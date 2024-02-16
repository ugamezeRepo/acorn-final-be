package com.acorn.finals.config;

import com.acorn.finals.annotation.WebSocketController;
import com.acorn.finals.annotation.WebSocketMapping;
import com.acorn.finals.annotation.WebSocketOnClose;
import com.acorn.finals.annotation.WebSocketOnConnect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSocket
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer, ApplicationContextAware {
    private static final String[] globalAllowedOrigins = {"http://localhost:5173"};
    private static ApplicationContext context;


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
                if (method.getParameterCount() > 1) continue;

                if (method.isAnnotationPresent(WebSocketMapping.class)) {
                    String additionalPath = method.getAnnotation(WebSocketMapping.class).value();
                    String fullPath = basePath + additionalPath;

                    Class<?> returnType = method.getReturnType();
                    Class<?> parameterType = method.getParameterCount() == 1 ? method.getParameterTypes()[0] : Void.class;

                    var currentWebSocketInfo = websocketMappings.computeIfAbsent(fullPath, k -> new WebSocketMappingInfo());
                    currentWebSocketInfo.addOnMessage(method, parameterType, returnType, bean, fullPath);
                }
                if (method.isAnnotationPresent(WebSocketOnConnect.class)) {
                    String additionalPath = method.getAnnotation(WebSocketOnConnect.class).value();
                    String fullPath = basePath + additionalPath;

                    var currentWebSocketInfo = websocketMappings.computeIfAbsent(fullPath, k -> new WebSocketMappingInfo());
                    currentWebSocketInfo.addOnConnect(method, bean, fullPath);
                }
                if (method.isAnnotationPresent(WebSocketOnClose.class)) {
                    String additionalPath = method.getAnnotation(WebSocketOnClose.class).value();
                    String fullPath = basePath + additionalPath;

                    var currentWebSocketInfo = websocketMappings.computeIfAbsent(fullPath, k -> new WebSocketMappingInfo());
                    currentWebSocketInfo.addOnClose(method, bean, fullPath);
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

    private static class WebSocketMappingHandler extends TextWebSocketHandler {
        WebSocketMappingInfo mappingInfo;

        private WebSocketMappingHandler(WebSocketMappingInfo mappingInfo) {
            this.mappingInfo = mappingInfo;
        }

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            for (int i = 0; i < mappingInfo.onConnect.size(); i++) {
                mappingInfo.onConnect.get(i).invoke(mappingInfo.onConnectObject.get(i));
            }
        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
            String payload = message.getPayload();

            for (int i = 0; i < mappingInfo.onMessage.size(); i++) {
                var parameterType = mappingInfo.onMessageParamTypes.get(i);
                var returnType = mappingInfo.onMessageReturnTypes.get(i);
                var serializedRequest = mapper.readValue(payload, parameterType);


                var invokeResult = mappingInfo.onMessage.get(i).invoke(mappingInfo.onMessageObject.get(i), serializedRequest);

                if (returnType.isInstance(invokeResult)) {
                    String result = null;
                    try {
                        var castedInvokeResult = returnType.cast(invokeResult);
                        result = mapper.writeValueAsString(castedInvokeResult);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    if (result != null) {
                        log.debug(result);
                        session.sendMessage(new TextMessage(result));
                    }
                }
            }
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            for (int i = 0; i < mappingInfo.onClose.size(); i++) {
                mappingInfo.onClose.get(i).invoke(mappingInfo.onCloseObject.get(i));
            }
        }
    }

    private static class WebSocketMappingInfo {
        // on connect
        List<Method> onConnect;
        List<Object> onConnectObject;
        List<String> onConnectFullPath;
        List<Class<?>[]> onConnectPathParamTypes;



        // on close
        List<Method> onClose;
        List<Object> onCloseObject;
        List<String> onCloseFullPath;
        List<Class<?>[]> onClosePathParamTypes;


        // on message
        List<Method> onMessage;
        List<Class<?>> onMessageParamTypes;
        List<Class<?>> onMessageReturnTypes;
        List<Object> onMessageObject;
        List<String> onMessageFullPath;
        List<Class<?>> onMessagePathParamTypes;


        WebSocketMappingInfo() {
            onConnect = new ArrayList<>();
            onConnectObject = new ArrayList<>();
            onConnectFullPath = new ArrayList<>();
            onConnectPathParamTypes = new ArrayList<>();

            onClose = new ArrayList<>();
            onCloseObject = new ArrayList<>();
            onCloseFullPath = new ArrayList<>();
            onClosePathParamTypes = new ArrayList<>();

            onMessage = new ArrayList<>();
            onMessageReturnTypes = new ArrayList<>();
            onMessageParamTypes = new ArrayList<>();
            onMessageObject = new ArrayList<>();
            onMessageFullPath = new ArrayList<>();
            onMessagePathParamTypes = new ArrayList<>();
        }

        WebSocketMappingInfo addOnConnect(Method method, Object obj, String fullPath) {
            onConnect.add(method);
            onConnectObject.add(obj);
            onConnectFullPath.add(fullPath);
            return this;
        }

        WebSocketMappingInfo addOnClose(Method method, Object obj, String fullPath) {
            onClose.add(method);
            onCloseObject.add(obj);
            onCloseFullPath.add(fullPath);
            return this;
        }

        WebSocketMappingInfo addOnMessage(Method method, Class<?> parameterType, Class<?> returnType, Object obj, String fullPath) {
            onMessage.add(method);
            onMessageParamTypes.add(parameterType);
            onMessageReturnTypes.add(returnType);
            onMessageObject.add(obj);
            onMessageFullPath.add(fullPath);
            return this;
        }
    }
}
