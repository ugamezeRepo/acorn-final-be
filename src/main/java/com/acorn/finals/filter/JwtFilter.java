package com.acorn.finals.filter;

import com.acorn.finals.config.properties.TokenPropertiesConfig;
import com.acorn.finals.model.AcornJwt;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final TokenPropertiesConfig tokenPropertiesConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // get access token from cookie
        var accessTokenJws = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("Authorization"))
                .map(cookie -> cookie.getValue().substring(7))
                .findFirst()
                .orElse(null);
        if (accessTokenJws == null) {
            filterChain.doFilter(request, response);
            return;
        }

        AcornJwt accessToken = AcornJwt.fromJws(accessTokenJws, tokenPropertiesConfig.getAccessToken().getSecret());
        if (accessToken != null && !accessToken.isExpired()) {
            var email = accessToken.getSubject();
            UserDetails ud = new User(email, "", List.of());
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }
}
