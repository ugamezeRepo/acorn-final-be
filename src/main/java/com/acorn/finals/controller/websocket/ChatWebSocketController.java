package com.acorn.finals.controller.websocket;

import com.acorn.finals.annotation.WebSocketController;
import com.acorn.finals.annotation.WebSocketMapping;
import com.acorn.finals.annotation.WebSocketOnConnect;
import com.acorn.finals.model.dto.MessageDto;
import com.acorn.finals.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@WebSocketController("/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {
    private final MessageService service;

    /**
     * client 가 websocket 으로  메시지를 보냈을때 할 처리
     *
     * @param dto 클래스의 필요한 정보 필여  url 에 담겨있는 내용은 따로 주실필요없습니다
     */
    @WebSocketMapping("/channel/{channelId}/topic/{topicId}")
    public MessageDto handleChatSend(@RequestBody MessageDto dto, @PathVariable int channelId, @PathVariable int topicId) {
        return service.receivedAndSend(dto, channelId, topicId);
    }

    @WebSocketOnConnect("/hello/{helloId}")
    public void onConnect() {
        log.debug("am i working");
    }
}
