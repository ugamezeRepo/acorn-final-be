package com.acorn.finals.controller.websocket;

import com.acorn.finals.annotation.WebSocketController;
import com.acorn.finals.annotation.WebSocketMapping;
import com.acorn.finals.model.dto.MessageDto;
import com.acorn.finals.model.dto.MessageReqDto;
import com.acorn.finals.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@WebSocketController("/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {
    private final MessageService service;

    /**
     * client 가 websocket 으로 메시지를 보냈을때 할 처리
     *
     * @param messageReqDto 클래스에 필요한 정보 필요 url 에 담겨있는 내용은 따로 주실필요없습니다.
     */
    @WebSocketMapping("/channel/{channelId}/topic/{topicId}")
    public ResponseEntity<MessageReqDto> handleChat(@RequestBody MessageReqDto messageReqDto,
                                                    @PathVariable int channelId, @PathVariable int topicId) {
        MessageDto messageDto = messageReqDto.getMessageDto();

        switch (messageReqDto.getJob()) {
            case "insert":
                messageDto = service.insertMsg(messageDto, channelId, topicId);
                messageReqDto.setMessageDto(messageDto);
                return ResponseEntity.ok(messageReqDto);
            case "update":
                service.updateMsg(messageDto);
                return ResponseEntity.ok(messageReqDto);
            case "delete":
                service.deleteMsg(messageDto);
                return ResponseEntity.ok(messageReqDto);
            default:
                return ResponseEntity.badRequest().build();
        }
    }
}
