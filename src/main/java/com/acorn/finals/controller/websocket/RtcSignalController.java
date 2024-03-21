package com.acorn.finals.controller.websocket;

import com.acorn.finals.annotation.WebSocketController;
import com.acorn.finals.annotation.WebSocketMapping;
import com.acorn.finals.annotation.WebSocketOnClose;
import com.acorn.finals.model.dto.websocket.RtcSignalDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.socket.WebSocketSession;


@AllArgsConstructor
@EqualsAndHashCode
class RoomInfo {
    int channelId;
    int topicId;
}


@WebSocketController("/webrtc")
@RequiredArgsConstructor
@Slf4j
public class RtcSignalController {
    private static final Map<RoomInfo, Map<WebSocketSession, String>> participantUUIDs = new HashMap<>();
    private final ObjectMapper objectMapper;

    @WebSocketMapping("/signal/{id}")
    public RtcSignalDto handleSignal(@RequestBody RtcSignalDto dto, @PathVariable String id) {
        log.debug("{}", dto);
        return dto;
    }

    @WebSocketMapping("/channel/{channelId}/topic/{topicId}")
    public RtcSignalDto handleTopicSignal(@RequestBody RtcSignalDto dto, @PathVariable int channelId,
                                          @PathVariable int topicId, WebSocketSession ws) {
        var roomInfo = new RoomInfo(channelId, topicId);
        participantUUIDs.computeIfAbsent(roomInfo, (k) -> new HashMap<>()).put(ws, dto.getUuid());
        return dto;
    }

    @WebSocketOnClose("/channel/{channelId}/topic/{topicId}")
    public RtcSignalDto handleTopicClose(@PathVariable int channelId, @PathVariable int topicId, WebSocketSession ws) {
        var roomInfo = new RoomInfo(channelId, topicId);
        var uuid = participantUUIDs.computeIfAbsent(roomInfo, (k) -> new HashMap<>()).remove(ws);
        return new RtcSignalDto(null, Map.of("type", "remove"), uuid);
    }
}
