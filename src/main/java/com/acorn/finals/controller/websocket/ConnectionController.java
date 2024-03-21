package com.acorn.finals.controller.websocket;

import com.acorn.finals.annotation.WebSocketController;
import com.acorn.finals.annotation.WebSocketMapping;
import com.acorn.finals.annotation.WebSocketOnClose;
import com.acorn.finals.annotation.WebSocketOnConnect;
import com.acorn.finals.model.WebSocketSessionInfo;
import com.acorn.finals.model.dto.MemberDto;
import com.acorn.finals.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.socket.WebSocketSession;

@WebSocketController("/connection")
@RequiredArgsConstructor
@Slf4j
public class ConnectionController {
    private static final Map<WebSocketSession, Integer> sessionMemberIdMapping = new HashMap<>();
    private static final Map<Integer, Integer> activeConnectionCount = new HashMap<>();
    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    @WebSocketMapping("/ping")
    public void connect(@RequestBody MemberDto myInfo, WebSocketSession session, WebSocketSessionInfo sessionInfo) {
        MemberDto member = memberService.findMemberById(myInfo.getId());
        sessionMemberIdMapping.put(session, member.getId());
        member.setStatus("online");
        memberService.updateStatus(member);
        var channelIds = memberService.findAllJoinedChannelIdByMember(myInfo);
        channelIds.forEach(id -> sessionInfo.sendAll(String.format("/connection/channel/%d/members", id),
                memberService.findAllMemberByChannelId(id), objectMapper));
    }

    @WebSocketOnClose("/ping")
    public void onClose(WebSocketSession session, WebSocketSessionInfo sessionInfo) {
        var memberId = sessionMemberIdMapping.get(session);
        if (memberId == null) return;
        var result = activeConnectionCount.computeIfPresent(memberId, (k, v) -> v - 1);
        if (result != null && result == 0) {
            var member = memberService.findMemberById(memberId);
            member.setStatus("offline");
            memberService.updateStatus(member);

            var channelIds = memberService.findAllJoinedChannelIdByMemberId(memberId);
            channelIds.forEach(id -> sessionInfo.sendAll(String.format("/connection/channel/%d/members", id),
                    memberService.findAllMemberByChannelId(id), objectMapper));
        }
    }

    @WebSocketOnConnect("/channel/{channelId}/members")
    public List<MemberDto> getChannelMembers(@PathVariable int channelId) {
        return memberService.findAllMemberByChannelId(channelId);
    }
}
