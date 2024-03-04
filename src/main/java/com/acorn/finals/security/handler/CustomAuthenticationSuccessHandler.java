package com.acorn.finals.security.handler;

import com.acorn.finals.config.properties.FrontendPropertiesConfig;
import com.acorn.finals.config.properties.TokenPropertiesConfig;
import com.acorn.finals.mapper.MemberMapper;
import com.acorn.finals.model.entity.MemberEntity;
import com.acorn.finals.service.MemberService;
import com.acorn.finals.service.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String[] adjectives = {"화려한", "창의적인", "열정적인", "자유로운", "활기찬", "격렬한", "쾌활한", "우아한", "현명한", "우아한", "정신없는", "신비로운", "풍부한", "안정된", "다채로운", "유쾌한", "진실한", "영리한", "열정적인", "차분한"};
    private static final String[] nouns = {"꽃", "나무", "바다", "하늘", "별", "빛", "음악", "사랑", "행복", "꿈", "모래", "강", "바람", "햇살", "눈", "비", "숲", "감성", "평화", "세상"};
    private final TokenService tokenService;
    private final MemberMapper memberMapper;
    private final TokenPropertiesConfig tokenPropertiesConfig;
    private final FrontendPropertiesConfig frontendPropertiesConfig;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            log.debug("on authentication success");
            OAuth2User user = oauthToken.getPrincipal();
            Map<String, Object> attributes = user.getAttributes();
            String email = (String) attributes.get("email");

            String accessToken = tokenService.createAccessTokenFromEmail(email);
            String refreshToken = tokenService.createRefreshTokenFromEmail(email);

            Cookie accessTokenCookie = new Cookie("Authorization", "Bearer+" + accessToken);
            accessTokenCookie.setMaxAge(tokenPropertiesConfig.getAccessToken().getExpiration());
            accessTokenCookie.setPath("/"); // 모든 경로에서 쿠키를 사용할수 있도록 설정

            Cookie refreshTokenCookie = new Cookie("RefreshToken", refreshToken);
            refreshTokenCookie.setMaxAge(tokenPropertiesConfig.getRefreshToken().getExpiration());
            refreshTokenCookie.setHttpOnly(true); // JavaScript에서 쿠키에 접근할 수 없도록 설정
            refreshTokenCookie.setPath("/"); // 모든 경로에서 쿠키를 사용할수 있도록 설정

            // 응답 헤더에 쿠키 추가
            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);

            // set-header http only refresh token
            // Oauth 로그인 후 Security 에서 부여되는 userName 을 다시 이메일로 지정하기 위한 로직
            UserDetails ud = new User(email, "", List.of());
            Authentication auth = new UsernamePasswordAuthenticationToken(ud, ud.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);


            if (memberMapper.findOneByEmail(email) == null) {
                boolean userCreated = false;
                final int maxFallback = 5;
                int currentFallback = 0;
                while (true) {
                    var rand = new Random();
                    var adjectiveIndex = rand.nextInt(0, 20);
                    var nounIndex = rand.nextInt(0, 20);
                    var nickname = String.join(" ", adjectives[adjectiveIndex], nouns[nounIndex]);
                    var hashtag = rand.nextInt(1000, 10000);
                    var result = memberService.signup(new MemberEntity(null, email, nickname, hashtag, "online"));
                    currentFallback += 1;
                    if (result || currentFallback > maxFallback) {
                        userCreated = result;
                        break;
                    }
                }

                if (!userCreated) {
                    throw new RuntimeException("user creation failed");
                }
//                response.sendRedirect(frontendPropertiesConfig.getUrl() + "/signup");
//                return;
            }
            response.sendRedirect(frontendPropertiesConfig.getUrl() + "/channel/@me");

        } else {
            // 다른 인증 방식인 경우 다른 처리를 수행할 수 있습니다.
            throw new RuntimeException("Unreachable!");
        }
    }
}
