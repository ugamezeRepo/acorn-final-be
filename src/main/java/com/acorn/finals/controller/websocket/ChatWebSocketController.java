package com.acorn.finals.controller.websocket;

import com.acorn.finals.annotation.WebSocketController;
import com.acorn.finals.annotation.WebSocketMapping;
import com.acorn.finals.annotation.WebSocketOnConnect;
import com.acorn.finals.model.dto.MessageDto;
import com.acorn.finals.model.entity.MessageEntity;
import com.acorn.finals.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;


@WebSocketController("/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {
    private final MessageService service;

    /**
     * client 가 websocket 으로  메시지를 보냈을때 할 처리
     * @param
     */
    @WebSocketMapping("/path/{pathId}")
    public MessageDto handleChatSend(MessageEntity entity, @PathVariable int pathId) {

        service.newMessageAdd(entity);
        MessageDto dto =service.receviedAndSend(entity);
        System.out.println(dto);
        return dto;
    }

    @WebSocketOnConnect("/hello/{helloId}")
    public void onConnect() {
        log.debug("am i working");
    }
}
