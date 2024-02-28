package com.acorn.finals.controller.websocket;

import com.acorn.finals.annotation.WebSocketController;
import com.acorn.finals.annotation.WebSocketMapping;
import com.acorn.finals.annotation.WebSocketOnConnect;
import com.acorn.finals.model.WebSocketSessionInfo;
import com.acorn.finals.model.dto.websocket.RtcSignalDto;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

@WebSocketController("/webrtc")
public class RtcSignalController {

    @WebSocketOnConnect("/signal")
    public Map<String, String> handleConnect(WebSocketSession session) {
        return Map.ofEntries(
                Map.entry("id", session.getId())
        );
    }
    @WebSocketMapping("/signal")
    public void handleSignal(@RequestBody RtcSignalDto dto, WebSocketSessionInfo sessionInfo) {
//        sessionInfo.
    }
}
