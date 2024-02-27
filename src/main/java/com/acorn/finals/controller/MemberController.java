package com.acorn.finals.controller;

import com.acorn.finals.model.dto.ChannelDto;
import com.acorn.finals.model.dto.MemberDto;
import com.acorn.finals.model.entity.MemberEntity;
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
    private final MemberDto mockUser = new MemberDto("admin@admin.com", "admin", 7777, null);

    /**
     * list all another user joined channel
     *
     * @param memberId id of the another member
     * @return list of channel
     */
    @GetMapping("/{memberId}/channel")
    public List<ChannelDto> listAllChannelOfAnotherMember(@PathVariable int memberId) {
        return null;
    }

    /**
     * query my information
     *
     * @return
     */
    @GetMapping("/@me")
    public MemberDto queryMyInfo() {
        return null;
    }

    /**
     * list all channel
     *
     * @return list of channel
     */
    @GetMapping("/@me/channel")
    public List<ChannelDto> listAllChannel() {
        // TODO: Authentication to MemberDto
        return memberService.listAllChannels(mockUser);
    }

    /**
     * list all friends of another user
     *
     * @param memberId
     * @return list of friend
     */
    @GetMapping("/{memberId}/friend")
    public List<MemberDto> listAllFriendsOfAnotherMember(@PathVariable int memberId) {
        return null;
    }


    /**
     * update my info
     *
     * @param dto
     * @return
     */
    @PatchMapping("/updateStatus")
    public ResponseEntity<Void> updateMyInfo(@RequestBody MemberDto dto) {
        memberService.updateStatus(dto);
        return ResponseEntity.ok(null);
    }

    /**
     * change nickname
     *
     * @param dto
     * @return
     */
    @PutMapping("/changeNick")
    public ResponseEntity<MemberDto> changeNick(@RequestBody MemberDto dto) {
        MemberDto responseDto = memberService.changeNickandTag(dto);
        return ResponseEntity.ok().body(responseDto);
    }

    /**
     * signup url
     *
     * @param entity require nickname , hashtag
     * @return boolean
     */
    @PostMapping("/signup")
    public boolean join(@RequestBody MemberEntity entity) {
        return memberService.signup(entity);
    }

}

