package com.acorn.finals.controller.websocket;

import com.acorn.finals.annotation.WebSocketController;
import com.acorn.finals.annotation.WebSocketMapping;
import com.acorn.finals.model.dto.MessageDto;
import com.acorn.finals.model.dto.MessageReqDto;
import com.acorn.finals.service.MessageChannelService;
import com.acorn.finals.service.MessageDMService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@WebSocketController("/chat")
@RequiredArgsConstructor
public class ChatWebSocketController {
    private final MessageChannelService messageChannelService;
    private final MessageDMService messageDMService;

    @WebSocketMapping("/dm/{dmId}")
    public ResponseEntity<MessageReqDto> handleChatOnDM(@PathVariable int dmId,
        @RequestBody MessageReqDto messageReqDto) {
        MessageDto messageDto = messageReqDto.getMessageDto();

        switch (messageReqDto.getJob()) {
            case "insert":
                messageDto = messageDMService.insertMsg(messageDto, dmId);
                messageReqDto.setMessageDto(messageDto);
                return ResponseEntity.status(HttpStatus.OK).body(messageReqDto);
            case "update":
                if (messageDMService.updateMsg(messageDto) == -1) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageReqDto);
                }
                return ResponseEntity.status(HttpStatus.OK).body(messageReqDto);
            case "delete":
                if (messageDMService.deleteMsg(messageDto) == -1) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageReqDto);
                }
                return ResponseEntity.status(HttpStatus.OK).body(messageReqDto);
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageReqDto);
        }
    }

    /**
     * client 가 websocket 으로 메시지를 보냈을때 할 처리
     *
     * @param messageReqDto 클래스에 필요한 정보 필요 url 에 담겨있는 내용은 따로 주실필요없습니다.
     */
    @WebSocketMapping("/channel/{channelId}/topic/{topicId}")
    public ResponseEntity<MessageReqDto> handleChatOnChannel(@PathVariable int channelId, @PathVariable int topicId,
            @RequestBody MessageReqDto messageReqDto) {
        MessageDto messageDto = messageReqDto.getMessageDto();

        switch (messageReqDto.getJob()) {
            case "insert":
                messageDto = messageChannelService.insertMsg(messageDto, channelId, topicId);
                messageReqDto.setMessageDto(messageDto);
                return ResponseEntity.status(HttpStatus.OK).body(messageReqDto);
            case "update":
                if (messageChannelService.updateMsg(messageDto) == -1) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageReqDto);
                }
                return ResponseEntity.status(HttpStatus.OK).body(messageReqDto);
            case "delete":
                if (messageChannelService.deleteMsg(messageDto) == -1) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageReqDto);
                }
                return ResponseEntity.status(HttpStatus.OK).body(messageReqDto);
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageReqDto);
        }
    }

}
