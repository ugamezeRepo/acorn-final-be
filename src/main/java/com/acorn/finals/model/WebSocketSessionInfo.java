package com.acorn.finals.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Set;

public class WebSocketSessionInfo extends HashMap<String, Set<WebSocketSession>> {
    public <T> void sendAll(String uri, T t, ObjectMapper mapper) {
        try {
            var msg = new TextMessage(mapper.writeValueAsString(t));
            var sessions = this.get(uri);
            if (sessions != null) {
                for (var sess : sessions) {
                    sess.sendMessage(msg);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
