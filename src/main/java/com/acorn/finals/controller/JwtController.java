package com.acorn.finals.controller;

import com.acorn.finals.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtController {
    private final JwtService jwtService;

    @GetMapping("/jwt")
    public String login(String userId) {
        return jwtService.generateToken("hohoho");

    }

    @GetMapping("/jwt/validation")
    public boolean jwtCheck(String token) {
        return jwtService.validateToken(token);
    }

    @GetMapping("jwt/test")
    public String jwtTest() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
