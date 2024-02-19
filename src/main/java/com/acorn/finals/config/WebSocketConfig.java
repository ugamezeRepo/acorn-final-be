package com.acorn.finals.config;

import com.acorn.finals.annotation.WebSocketController;
import com.acorn.finals.annotation.WebSocketMapping;
import com.acorn.finals.annotation.WebSocketOnClose;
import com.acorn.finals.annotation.WebSocketOnConnect;
import com.acorn.finals.util.PathUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.net.URI;
import java.util.*;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer, ApplicationContextAware {
    private static final String[] globalAllowedOrigins = {"http://localhost:5173"};
    private static ApplicationContext context;

    private final ServletContext servletContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        Map<String, Object> beans = context.getBeansWithAnnotation(WebSocketController.class);
        Map<String, WebSocketMappingInfo> websocketMappings = new HashMap<>();

        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            String basePath = entry.getKey();
            Object bean = entry.getValue();

            Method[] methods = bean.getClass().getMethods();
            for (var method : methods) {
                if (method.isAnnotationPresent(WebSocketMapping.class)) {
                    String additionalPath = method.getAnnotation(WebSocketMapping.class).value();
                    String path = basePath + additionalPath;
                    var currentWebSocketInfo = websocketMappings.computeIfAbsent(path, k -> new WebSocketMappingInfo());
                    currentWebSocketInfo.addOnMessage(method, bean, path);
                }
                if (method.isAnnotationPresent(WebSocketOnConnect.class)) {
                    String additionalPath = method.getAnnotation(WebSocketOnConnect.class).value();
                    String path = basePath + additionalPath;
                    var currentWebSocketInfo = websocketMappings.computeIfAbsent(path, k -> new WebSocketMappingInfo());
                    currentWebSocketInfo.addOnConnect(method, bean, path);
                }
                if (method.isAnnotationPresent(WebSocketOnClose.class)) {
                    String additionalPath = method.getAnnotation(WebSocketOnClose.class).value();
                    String path = basePath + additionalPath;
                    var currentWebSocketInfo = websocketMappings.computeIfAbsent(path, k -> new WebSocketMappingInfo());
                    currentWebSocketInfo.addOnClose(method, bean, path);
                }
            }
        }

        for (Map.Entry<String, WebSocketMappingInfo> entry : websocketMappings.entrySet()) {
            String path = entry.getKey();
            WebSocketMappingInfo mappingInfo = entry.getValue();

            registry.addHandler(new WebSocketMappingHandler(mappingInfo), path)
                    .setAllowedOrigins(globalAllowedOrigins);
        }
    }

    private static class WebSocketMappingInfo {
        List<Method> onConnect;
        List<Object> onConnectObject;
        List<String> onConnectPath;

        List<Method> onClose;
        List<Object> onCloseObject;
        List<String> onClosePath;

        List<Method> onMessage;
        List<Object> onMessageObject;
        List<String> onMessagePath;

        WebSocketMappingInfo() {
            onConnect = new ArrayList<>();
            onConnectObject = new ArrayList<>();
            onConnectPath = new ArrayList<>();

            onClose = new ArrayList<>();
            onCloseObject = new ArrayList<>();
            onClosePath = new ArrayList<>();

            onMessage = new ArrayList<>();
            onMessageObject = new ArrayList<>();
            onMessagePath = new ArrayList<>();
        }

        public WebSocketMappingInfo addOnConnect(Method method, Object obj, String path) {
            onConnect.add(method);
            onConnectObject.add(obj);
            onConnectPath.add(path);
            return this;
        }

        public WebSocketMappingInfo addOnClose(Method method, Object obj, String path) {
            onClose.add(method);
            onCloseObject.add(obj);
            onClosePath.add(path);
            return this;
        }

        public WebSocketMappingInfo addOnMessage(Method method, Object obj, String path) {
            onMessage.add(method);
            onMessageObject.add(obj);
            onMessagePath.add(path);
            return this;
        }

    }

    private class WebSocketMappingHandler extends TextWebSocketHandler {
        private static Map<URI, Set<WebSocketSession>> sessionInfo;
        WebSocketMappingInfo mappingInfo;

        private WebSocketMappingHandler(WebSocketMappingInfo mappingInfo) {
            this.mappingInfo = mappingInfo;
            sessionInfo = new HashMap<>();
        }

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            var sessionSet = sessionInfo.computeIfAbsent(session.getUri(), uri -> new HashSet<>());
            sessionSet.add(session);

            ObjectMapper mapper = new ObjectMapper();
            var requestPath = session.getUri().getPath().substring(servletContext.getContextPath().length());
            mappingLoop:
            for (int i = 0; i < mappingInfo.onConnect.size(); i++) {
                var methodInfo = mappingInfo.onConnect.get(i);
                var methodSubject = mappingInfo.onConnectObject.get(i);
                var pathPattern = mappingInfo.onConnectPath.get(i);

                var methodParameters = methodInfo.getParameters();
                var returnType = methodInfo.getReturnType();

                var pathMap = PathUtils.extractPath(pathPattern, requestPath);

                List<Object> params = new ArrayList<>();
                for (var parameter : methodParameters) {
                    if (!parameter.isAnnotationPresent(PathVariable.class)) {
                        continue mappingLoop;
                    }
                    try {
                        var pathAnnotation = parameter.getAnnotation(PathVariable.class);
                        var pathIdentifier = pathAnnotation.value().isEmpty()
                                ? parameter.getName()
                                : pathAnnotation.value();

                        String pathValue = pathMap.get(pathIdentifier);
                        if (parameter.getType().equals(String.class)) {
                            pathValue = "\"" + pathValue + "\"";
                        }
                        var serializedPathValue = mapper.readValue(pathValue, parameter.getType());
                        params.add(serializedPathValue);

                    } catch (Exception e) {
                        log.error(e.getMessage());
                        continue mappingLoop;
                    }
                }

                var invokeResult = methodInfo.invoke(methodSubject, params.toArray());
                if (returnType.isInstance(invokeResult)) {
                    String result = null;
                    try {
                        var castedInvokeResult = returnType.cast(invokeResult);
                        result = mapper.writeValueAsString(castedInvokeResult);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    if (result != null) {
                        var resultMessage = new TextMessage(result);
                        var sessions = sessionInfo.get(session.getUri());
                        if (sessions != null) {
                            for (var sess : sessions) {
                                sess.sendMessage(resultMessage);
                            }
                        }
                    }
                }
            }
        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
            String payload = message.getPayload();

            var requestPath = session.getUri().getPath().substring(servletContext.getContextPath().length());
            mappingLoop:
            for (int i = 0; i < mappingInfo.onMessage.size(); i++) {
                var methodInfo = mappingInfo.onMessage.get(i);
                var methodSubject = mappingInfo.onMessageObject.get(i);
                var pathPattern = mappingInfo.onMessagePath.get(i);

                var methodParameters = methodInfo.getParameters();
                var returnType = methodInfo.getReturnType();

                var pathMap = PathUtils.extractPath(pathPattern, requestPath);

                // create params
                List<Object> params = new ArrayList<>();
                for (var parameter : methodParameters) {
                    if (parameter.isAnnotationPresent(RequestBody.class)) {
                        // handle request dto from text message
                        try {
                            var serialized = mapper.readValue(payload, parameter.getType());
                            params.add(serialized);
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

                            String pathValue = pathMap.get(pathIdentifier);
                            if (parameter.getType().equals(String.class)) {
                                pathValue = "\"" + pathValue + "\"";
                            }
                            var serializedPathValue = mapper.readValue(pathValue, parameter.getType());
                            params.add(serializedPathValue);
                        } catch (Exception e) {
                            log.error(e.getMessage());
                            continue mappingLoop;
                        }
                    } else {
                        continue mappingLoop;
                    }
                }

                var invokeResult = methodInfo.invoke(methodSubject, params.toArray());
                if (returnType.isInstance(invokeResult)) {
                    String result = null;
                    try {
                        var castedInvokeResult = returnType.cast(invokeResult);
                        result = mapper.writeValueAsString(castedInvokeResult);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    if (result != null) {
                        var resultMessage = new TextMessage(result);
                        var sessions = sessionInfo.get(session.getUri());
                        if (sessions != null) {
                            for (var sess : sessions) {
                                sess.sendMessage(resultMessage);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            var sessionSet = sessionInfo.computeIfAbsent(session.getUri(), uri -> new HashSet<>());
            sessionSet.remove(session);

            ObjectMapper mapper = new ObjectMapper();
            var requestPath = session.getUri().getPath().substring(servletContext.getContextPath().length());
            mappingLoop:
            for (int i = 0; i < mappingInfo.onClose.size(); i++) {
                var methodInfo = mappingInfo.onClose.get(i);
                var methodSubject = mappingInfo.onCloseObject.get(i);
                var pathPattern = mappingInfo.onClosePath.get(i);

                var methodParameters = methodInfo.getParameters();
                var returnType = methodInfo.getReturnType();

                var pathMap = PathUtils.extractPath(pathPattern, requestPath);

                List<Object> params = new ArrayList<>();
                for (var parameter : methodParameters) {
                    if (!parameter.isAnnotationPresent(PathVariable.class)) {
                        continue mappingLoop;
                    }
                    try {
                        var pathAnnotation = parameter.getAnnotation(PathVariable.class);
                        var pathIdentifier = pathAnnotation.value().isEmpty()
                                ? parameter.getName()
                                : pathAnnotation.value();

                        String pathValue = pathMap.get(pathIdentifier);
                        if (parameter.getType().equals(String.class)) {
                            pathValue = "\"" + pathValue + "\"";
                        }
                        var serializedPathValue = mapper.readValue(pathValue, parameter.getType());
                        params.add(serializedPathValue);

                    } catch (Exception e) {
                        log.error(e.getMessage());
                        continue mappingLoop;
                    }
                }

                var invokeResult = methodInfo.invoke(methodSubject, params.toArray());
                if (returnType.isInstance(invokeResult)) {
                    String result = null;
                    try {
                        var castedInvokeResult = returnType.cast(invokeResult);
                        result = mapper.writeValueAsString(castedInvokeResult);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    if (result != null) {
                        var resultMessage = new TextMessage(result);
                        var sessions = sessionInfo.get(session.getUri());
                        if (sessions != null) {
                            for (var sess : sessions) {
                                sess.sendMessage(resultMessage);
                            }
                        }
                     }
                }
            }
        }
    }
}
