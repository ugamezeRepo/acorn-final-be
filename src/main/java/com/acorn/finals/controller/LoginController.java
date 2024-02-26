package com.acorn.finals.controller;

import com.acorn.finals.mapper.MemberMapper;
import com.acorn.finals.model.entity.MemberEntity;
import com.acorn.finals.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final MemberMapper memberMapper;
    private final MemberService memberService;

    @PostMapping("/join")
    public boolean join(@RequestBody MemberEntity entity) {
        return memberService.join(entity);
    }

}
