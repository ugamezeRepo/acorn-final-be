package com.acorn.finals.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        // 로그인 실패 후 실행될 동작을 여기에 구현합니다.
        System.out.println("Login failed: " + exception.getMessage());

        // 로그인 실패 후 리다이렉트 등 다른 동작을 수행할 수 있습니다.
        response.sendRedirect("/login?error"); // 실패시 login 페이지로 리다이렉트, 실패 메시지를 query parameter로 전달할 수도 있습니다.
    }
}