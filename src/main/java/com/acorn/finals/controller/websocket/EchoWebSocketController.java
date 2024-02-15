package com.acorn.finals.controller.websocket;

import com.acorn.finals.annotation.WebSocketController;
import com.acorn.finals.annotation.WebSocketMapping;
import com.acorn.finals.annotation.WebSocketOnClose;
import com.acorn.finals.annotation.WebSocketOnConnect;
import com.acorn.finals.model.dto.WebSocketDummyDto;
import lombok.extern.slf4j.Slf4j;

@WebSocketController("/ws")
@Slf4j
public class EchoWebSocketController {
    @WebSocketOnConnect
    public void onConnect() {
        log.debug("Connected!");
    }

    @WebSocketOnClose
    public void onClose() {
        log.debug("Closed!");
    }

    @WebSocketMapping("/echo")
    public String echo(String input) {
        return input;
    }

    @WebSocketMapping("/echo-with-tail")
    public String echoWithTail(String input) {
        return input + "~";
    }

    @WebSocketMapping("/plus-one")
    public WebSocketDummyDto plusOne(WebSocketDummyDto dto) {
        return new WebSocketDummyDto("response", dto.getValue() + 1);
    }
}
