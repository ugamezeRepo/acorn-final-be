package com.acorn.finals.controller.websocket;

import com.acorn.finals.annotation.WebSocketController;
import com.acorn.finals.annotation.WebSocketMapping;
import com.acorn.finals.model.dto.WebSocketDummyDto;

@WebSocketController("/ws")
public class EchoWebSocketController {
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
