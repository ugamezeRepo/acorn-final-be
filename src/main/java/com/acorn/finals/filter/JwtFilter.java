package com.acorn.finals.filter;

import com.acorn.finals.mapper.MemberMapper;
import com.acorn.finals.service.CustomUserDetailService;
import com.acorn.finals.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailService userService;
    private final MemberMapper memberMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        Cookie[] cookies = request.getCookies();
        String Authorization = "Authorization";
        String token = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Authorization.equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        String userEmail = null;
        if (token.startsWith("Bearer+")) {
            //앞에 "Bearer " 를 제외한 순수 토큰 문자열 얻어내기
            token = token.substring(7);

            userEmail = jwtService.extractUserName(token);
        }
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(userEmail);
            boolean isValid = jwtService.validateToken(token);
            //만일 유효하다면 1회성 로그인 처리를한다
            if (isValid) {
                //사용자가 제출한 사용자 이름과 비밀번호와 같은 인증 자격 증명을 저장
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //Security 컨텍스트 업데이트 (1회성 로그인)
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }

        filterChain.doFilter(request, response);
    }
}
