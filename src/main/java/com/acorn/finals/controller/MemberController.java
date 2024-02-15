package com.acorn.finals.controller;

import com.acorn.finals.model.dto.ChannelDto;
import com.acorn.finals.model.dto.MemberDto;
import com.acorn.finals.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /**
     * list all another user joined channel
     * @param memberId id of the another member
     * @return list of channel
     */
    @GetMapping("/{memberId}/channel")
    public List<ChannelDto> listAllChannelOfAnotherMember(@PathVariable int memberId) {
        return null;
    }

    /**
     * query my information
     * @return
     */
    @GetMapping("/@me")
    public MemberDto queryMyInfo() {
        return null;
    }

    /**
     * list all channel
     * @return list of channel
     */
    @GetMapping("/@me/channel")
    public List<ChannelDto> listAllChannel() {
        return null;
    }

    /**
     * list all friends of another user
     * @param memberId
     * @return list of friend
     */
    @GetMapping("/{memberId}/friend")
    public List<MemberDto> listAllFriendsOfAnotherMember(@PathVariable int memberId) {
        return null;
    }


    /**
     * change my nickname
     * @param updateMemberRequest new nickname
     * @return HTTP STATUS 200 on successful
     */
    @PatchMapping("/@me")
    public ResponseEntity<Void> updateMyInfo(@PathVariable MemberDto updateMemberRequest) {
        return ResponseEntity.ok(null);
    }
}
