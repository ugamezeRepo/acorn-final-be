package com.acorn.finals.filter;

import com.acorn.finals.mapper.MemberMapper;
import com.acorn.finals.mapper.RefreshTokenMapper;
import com.acorn.finals.service.CustomUserDetailService;
import com.acorn.finals.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final CustomUserDetailService userService;
    private final MemberMapper memberMapper;
    private final RefreshTokenMapper refreshTokenMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        final String Authorization = "Authorization";

        // get access token & refresh token from cookie
        String accessToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Authorization.equals(cookie.getName())) {
                    accessToken = cookie.getValue().substring(7);
                }
            }
        }

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String userEmail = tokenService.extractEmailFromJws(accessToken);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByEmail(userEmail);

            // access token 이 존재
            boolean isValid = tokenService.validateToken(accessToken);
            if (isValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
