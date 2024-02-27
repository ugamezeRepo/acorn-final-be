package com.acorn.finals.controller;

import com.acorn.finals.model.dto.AccessTokenDto;
import com.acorn.finals.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("token")
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/issue")
    public AccessTokenDto issueAccessToken(@RequestHeader("x-refresh-token") String refreshToken) {
        return tokenService.issueAccessToken(refreshToken);
    }

//    @GetMapping("/jwt")
//    public String login(String userId) {
//        return tokenService.generateToken("hohoho");
//
//    }
//
//    @GetMapping("/jwt/validation")
//    public boolean jwtCheck(String token) {
//        return tokenService.validateToken(token);
//    }
//
//    @GetMapping("jwt/test")
//    public String jwtTest() {
//        return SecurityContextHolder.getContext().getAuthentication().getName();
//    }
//
//    @GetMapping("jwt/refresh")
//    public String refresh() {
//        return tokenService.createRefreshToken("son");
//    }
}
