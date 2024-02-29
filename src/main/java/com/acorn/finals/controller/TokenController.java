package com.acorn.finals.controller;

import com.acorn.finals.config.properties.TokenPropertiesConfig;
import com.acorn.finals.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("token")
public class TokenController {
    private final TokenService tokenService;
    private final TokenPropertiesConfig tokenPropertiesConfig;

    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshAccessToken(@CookieValue("RefreshToken") String refreshToken) {
        String accessToken = tokenService.createAccessTokenFromRefreshToken(refreshToken);
        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ResponseCookie accessTokenCookie = ResponseCookie
                .from("Authorization", "Bearer+" + accessToken)
                .path("/")
                .maxAge(tokenPropertiesConfig.getAccessToken().getExpiration())
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString()).build();
    }
}
