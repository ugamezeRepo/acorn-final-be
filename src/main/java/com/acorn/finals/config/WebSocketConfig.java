package com.acorn.finals.config;

import com.acorn.finals.annotation.WebSocketController;
import com.acorn.finals.annotation.WebSocketMapping;
import com.acorn.finals.annotation.WebSocketOnClose;
import com.acorn.finals.annotation.WebSocketOnConnect;
import com.acorn.finals.config.properties.CorsPropertiesConfig;
import com.acorn.finals.model.WebSocketSessionInfo;
import com.acorn.finals.util.PathUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer, ApplicationContextAware {
    private static ApplicationContext context;
    private final CorsPropertiesConfig corsConfig;
    private final ServletContext servletContext;
    private final ObjectMapper objectMapper;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        Map<String, Object> beans = context.getBeansWithAnnotation(WebSocketController.class);
        Map<String, WebSocketMappingMetadata> websocketMappings = new HashMap<>();

        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            String basePath = entry.getKey();
            Object bean = entry.getValue();

            Method[] methods = bean.getClass().getMethods();
            for (var method : methods) {
                handleMethodEachAnnotation(basePath, method, WebSocketOnConnect.class, (additionalPath, path) ->
                    websocketMappings.computeIfAbsent(path, k -> new WebSocketMappingMetadata())
                        .addOnConnect(method, bean, path)
                );

                handleMethodEachAnnotation(basePath, method, WebSocketOnClose.class, (additionalPath, path) ->
                    websocketMappings.computeIfAbsent(path, k -> new WebSocketMappingMetadata())
                        .addOnClose(method, bean, path)
                );

                handleMethodEachAnnotation(basePath, method, WebSocketMapping.class, (additionalPath, path) ->
                    websocketMappings.computeIfAbsent(path, k -> new WebSocketMappingMetadata())
                        .addOnMessage(method, bean, path)
                );
            }
        }

        for (Map.Entry<String, WebSocketMappingMetadata> entry : websocketMappings.entrySet()) {
            String path = entry.getKey();
            WebSocketMappingMetadata mappingInfo = entry.getValue();

            log.debug("Add websocket handler on {}", path);
            registry.addHandler(new WebSocketMappingHandler(mappingInfo), path)
                .setAllowedOrigins(corsConfig.getAllowedOrigins());
        }
    }

    private void handleMethodEachAnnotation(String basePath, Method method, Class annotationClass, BiConsumer<String, String> action) {
        try {
            if (method.isAnnotationPresent(annotationClass)) {
                String additionalPath = (String) annotationClass.getMethod("value").invoke(method.getAnnotation(annotationClass));
                String path = basePath + additionalPath;
                action.accept(additionalPath, path);
            }
        } catch (Exception e) {
            log.error("Failed to get value from annotation", e);
        }
    }

    private static class WebSocketMappingMetadata {
        List<WebSocketMetadata> onConnect;
        List<WebSocketMetadata> onClose;
        List<WebSocketMetadata> onMessage;

        WebSocketMappingMetadata() {
            onConnect = new ArrayList<>();
            onClose  = new ArrayList<>();
            onMessage = new ArrayList<>();
        }

        public WebSocketMappingMetadata addOnConnect(Method method, Object obj, String path) {
            onConnect.add(new WebSocketMetadata(method, obj, path));
            return this;
        }

        public WebSocketMappingMetadata addOnClose(Method method, Object obj, String path) {
            onClose.add(new WebSocketMetadata(method, obj, path));
            return this;
        }

        public WebSocketMappingMetadata addOnMessage(Method method, Object obj, String path) {
            onMessage.add(new WebSocketMetadata(method, obj, path));
            return this;
        }
    }

    private static class WebSocketMetadata {
        Method methodInfo;
        Object obj;
        String path;

        public WebSocketMetadata(Method methodInfo, Object obj, String path) {
            this.methodInfo = methodInfo;
            this.obj = obj;
            this.path = path;
        }
    }

    private class WebSocketMappingHandler extends TextWebSocketHandler {
        private static final WebSocketSessionInfo sessionInfo = new WebSocketSessionInfo();
        private final WebSocketMappingMetadata webSocketMappingMetadata;

        private WebSocketMappingHandler(WebSocketMappingMetadata webSocketMappingMetadata) {
            this.webSocketMappingMetadata = webSocketMappingMetadata;
        }

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            var requestPath = PathUtils.convertUriToPathExceptForContextPath(session.getUri(), servletContext);
            var sessionSet = sessionInfo.computeIfAbsent(requestPath, uri -> new HashSet<>());
            sessionSet.add(session);
            invokeMethods(webSocketMappingMetadata.onConnect, session, requestPath, null);
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            var requestPath = PathUtils.convertUriToPathExceptForContextPath(session.getUri(), servletContext);
            var sessionSet = sessionInfo.computeIfAbsent(requestPath, uri -> new HashSet<>());
            sessionSet.remove(session);
            invokeMethods(webSocketMappingMetadata.onClose, session, requestPath, null);
        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            var requestPath = PathUtils.convertUriToPathExceptForContextPath(session.getUri(), servletContext);
            invokeMethods(webSocketMappingMetadata.onMessage, session, requestPath, message);
        }

        private void invokeMethods(List<WebSocketMetadata> webSocketMetadataList, WebSocketSession session, String requestPath, TextMessage message) throws Exception {
            mappingLoop:
            for (WebSocketMetadata webSocketMetadata : webSocketMetadataList) {
                var methodInfo = webSocketMetadata.methodInfo;
                var methodSubject = webSocketMetadata.obj;
                var pathPattern = webSocketMetadata.path;

                var methodParameters = methodInfo.getParameters();
                var returnType = methodInfo.getReturnType();

                List<Object> params = new ArrayList<>();
                for (var parameter : methodParameters) {
                    if (parameter.getType().equals(WebSocketSession.class)) {
                        params.add(session);
                    } else if (parameter.getType().equals(WebSocketSessionInfo.class)) {
                        params.add(sessionInfo);
                    } else if (parameter.isAnnotationPresent(RequestBody.class)) {
                        try {
                            params.add(objectMapper.readValue(message.getPayload(), parameter.getType()));
                        } catch (Exception e) {
                            log.error(e.getMessage());
                            continue mappingLoop;
                        }
                    } else if (parameter.isAnnotationPresent(PathVariable.class)) {
                        try {
                            var pathAnnotation = parameter.getAnnotation(PathVariable.class);
                            var pathIdentifier = pathAnnotation.value().isEmpty()
                                ? parameter.getName()
                                : pathAnnotation.value();

                            Map<String, String> pathMap = PathUtils.extractPath(pathPattern, requestPath);
                            String pathValue = pathMap.get(pathIdentifier);
                            if (parameter.getType().equals(String.class)) {
                                pathValue = "\"" + pathValue + "\"";
                            }
                            params.add(objectMapper.readValue(pathValue, parameter.getType()));
                        } catch (Exception e) {
                            log.error(e.getMessage());
                            continue mappingLoop;
                        }
                    } else {
                        continue mappingLoop;
                    }
                }

                if (returnType.equals(void.class) || returnType.equals(Void.class)) {
                    methodInfo.invoke(methodSubject, params.toArray());
                    continue;
                }
                var invokeResult = methodInfo.invoke(methodSubject, params.toArray());
                if (returnType.isInstance(invokeResult)) {
                    try {
                        var castedInvokeResult = returnType.cast(invokeResult);
                        sessionInfo.sendAll(requestPath, castedInvokeResult, objectMapper);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }

    }
}
