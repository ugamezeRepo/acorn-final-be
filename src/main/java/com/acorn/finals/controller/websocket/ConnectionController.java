package com.acorn.finals.controller.websocket;

import com.acorn.finals.annotation.WebSocketController;
import com.acorn.finals.annotation.WebSocketMapping;
import com.acorn.finals.annotation.WebSocketOnClose;
import com.acorn.finals.annotation.WebSocketOnConnect;
import com.acorn.finals.model.WebSocketSessionInfo;
import com.acorn.finals.model.dto.MemberDto;
import com.acorn.finals.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebSocketController("/connection")
@RequiredArgsConstructor
@Slf4j
public class ConnectionController {
    private static final Map<WebSocketSession, MemberDto> activeConnection = new HashMap<>();
    private final MemberService memberService;

    private final ObjectMapper objectMapper;
    private final Map<String, Integer> currentConnectionAmount = new HashMap<>();
    private String email;

    @WebSocketMapping("/ping")
    public void connect(@RequestBody MemberDto myInfo, WebSocketSession session, WebSocketSessionInfo sessionInfo) {
        email = myInfo.getEmail();

//        if (!myInfo.getStatus().equals("offline")) {
//            int connectionAmount = currentConnectionAmount.get(myInfo.getEmail());
//            currentConnectionAmount.replace(myInfo.getEmail(), connectionAmount + 1);
//        } else {
//            currentConnectionAmount.put(myInfo.getEmail(), 1);
            myInfo.setStatus("online");
            memberService.updateStatus(myInfo);
            activeConnection.put(session, myInfo);
            var channelIds = memberService.findAllJoinedChannelIdByMember(myInfo);

            channelIds.forEach(id -> sessionInfo.sendAll(
                String.format("/connection/channel/%d/members", id),
                memberService.findAllMemberByChannelId(id),
                objectMapper)
            );
//        }
    }

    @WebSocketOnClose("/ping")
    public void onClose(WebSocketSession session, WebSocketSessionInfo sessionInfo) {
        var userInfo = activeConnection.remove(session);
        if (userInfo == null) return;

//        if (currentConnectionAmount.get(email) - 1 > 0) {
//            int connectionAmount = currentConnectionAmount.get(email);
//            currentConnectionAmount.replace(email, connectionAmount - 1);
//        } else {
//            currentConnectionAmount.remove(email);

            userInfo.setStatus("offline");
            memberService.updateStatus(userInfo);

            var channelIds = memberService.findAllJoinedChannelIdByMember(userInfo);
            channelIds.forEach(id -> sessionInfo.sendAll(
                String.format("/connection/channel/%d/members", id),
                memberService.findAllMemberByChannelId(id),
                objectMapper)
            );
//        }
    }

    @WebSocketOnConnect("/channel/{channelId}/members")
    public List<MemberDto> getChannelMembers(@PathVariable int channelId) {
        return memberService.findAllMemberByChannelId(channelId);
    }
}
