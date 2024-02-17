package com.acorn.finals.controller.websocket;

import com.acorn.finals.annotation.WebSocketController;
import com.acorn.finals.annotation.WebSocketMapping;
import com.acorn.finals.annotation.WebSocketOnClose;
import com.acorn.finals.annotation.WebSocketOnConnect;
import com.acorn.finals.model.dto.ChannelDto;
import com.acorn.finals.model.dto.WebSocketDummyDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@WebSocketController("/test")
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
    public String echo(@RequestBody String input) {
        return input;
    }

    @WebSocketMapping("/echo-with-tail")
    public String echoWithTail(@RequestBody String input) {
        return input + "~";
    }

    /**
     * @param dto {"type": "string", "value": 1 }
     * @return
     */
    @WebSocketMapping("/plus-one")
    public WebSocketDummyDto plusOne(@RequestBody WebSocketDummyDto dto) {
        return new WebSocketDummyDto("response", dto.getValue() + 1);
    }

    /**
     * @param channelId {"name": "string", "thumbnail": "string2"}
     * @param pathId
     * @param dto
     * @return
     */
    @WebSocketMapping("/channel/{channelId}/path/{pathId}")
    public String test(@PathVariable int channelId, @PathVariable String pathId, @RequestBody ChannelDto dto) {
        return String.format("channelId = %d, pathId = %s, dto = %s", channelId, pathId, dto.toString());
    }
}
