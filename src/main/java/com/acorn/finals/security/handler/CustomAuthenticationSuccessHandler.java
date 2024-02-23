package com.acorn.finals.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> userService;

    public CustomAuthenticationSuccessHandler(OAuth2UserService<OAuth2UserRequest, OAuth2User> userService) {
        this.userService = userService;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            OAuth2User user = oauthToken.getPrincipal();

            Map<String, Object> attributes = user.getAttributes();
            String email = (String) attributes.get("email");

            // 로그인 성공 후 동작 예시: 이메일이나 사용자 정보를 기반으로 추가 작업 수행
            System.out.println("User logged in: " + email);

            // set-header http only refresh token

            // 로그인 성공 후 리다이렉트 등 다른 동작을 수행할 수 있습니다.
            response.sendRedirect("https://naver.com");
        } else {
            // 다른 인증 방식인 경우 다른 처리를 수행할 수 있습니다.
            response.sendRedirect("/api");
        }
    }
}
