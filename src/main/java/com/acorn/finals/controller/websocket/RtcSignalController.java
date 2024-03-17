package com.acorn.finals.controller.websocket;

import com.acorn.finals.annotation.WebSocketController;
import com.acorn.finals.annotation.WebSocketMapping;
import com.acorn.finals.model.dto.websocket.RtcSignalDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@WebSocketController("/webrtc")
@Slf4j
public class RtcSignalController {
    @WebSocketMapping("/signal/{id}")
    public RtcSignalDto handleSignal(@RequestBody RtcSignalDto dto, @PathVariable String id) {
        log.debug("{}", dto);
        return dto;
    }
}
