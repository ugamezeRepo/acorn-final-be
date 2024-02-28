package com.acorn.finals.service;

import com.acorn.finals.config.properties.TokenPropertiesConfig;
import com.acorn.finals.mapper.RefreshTokenMapper;
import com.acorn.finals.model.dto.AccessTokenDto;
import com.acorn.finals.model.entity.RefreshTokenEntity;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {
    private final TokenPropertiesConfig tokenPropertiesConfig;
    private final RefreshTokenMapper refreshTokenMapper;
    SecretKey key;
    String secret;
    private String secretString;
    private String encodedSecretString;

    private long expiration;

    @PostConstruct
    public void init() {
        secretString = tokenPropertiesConfig.getAccessToken().getSecret();
        expiration = tokenPropertiesConfig.getAccessToken().getExpiration();
        encodedSecretString = Encoders.BASE64.encode(secretString.getBytes(StandardCharsets.UTF_8));
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(encodedSecretString));
        secret = Encoders.BASE64.encode(key.getEncoded());
    }

    public String createAccessToken(Map<String, Object> claims, String subject) {

        log.debug(secret); // secret key here
        return Jwts.builder()
                .setClaims(claims)
                .subject(subject)
                .signWith(key)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    @Transactional
    public String createRefreshToken(String userEmail) {
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setEmail(userEmail);
        entity.setExpireDate(LocalDateTime.now().plusSeconds(tokenPropertiesConfig.getRefreshToken().getExpiration()));
        entity.setToken(UUID.randomUUID().toString());

        var existingToken = refreshTokenMapper.findOneTokenByEmail(userEmail);
        if (existingToken == null) {
            refreshTokenMapper.insert(entity);
        } else {
            entity.setId(existingToken.getId());
            refreshTokenMapper.update(entity);
        }
        return entity.getToken();
    }

    public String generateToken(String subject) {
        Map<String, Object> claims = new HashMap<>();
        //테스트로 추가 정보도 담아보기
        claims.put("email", "naver@");
        claims.put("addr", "서울시 강남구");
        return createAccessToken(claims, subject);
    }

    public String extractEmailFromJws(String accessToken) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken).getPayload().getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date ExpirationTime = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration();
        return ExpirationTime.before(new Date()); //시간 지나면 false
    }


    public boolean validateToken(String token) {
        boolean claim;
        String userName = extractEmailFromJws(token);
        try {
            var parsedJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            claim = parsedJws.getPayload().getSubject().equals(userName);
//            System.out.println(parsedJws.getPayload().getIssuedAt());
//            System.out.println(parsedJws.getPayload().getSubject());
//            System.out.println(parsedJws.getPayload().getExpiration());
//            System.out.println(parsedJws.getPayload().get("email"));
//            System.out.println(parsedJws.getPayload().get("addr"));
//            System.out.println(Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody());
            //OK, we can trust this JWT

        } catch (JwtException e) {

            claim = false;
        }
        return claim && !isTokenExpired(token);
    }


    public AccessTokenDto issueAccessToken(String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenMapper.findOneTokenByToken(refreshToken);

        // refresh token entity 가 없거나, 만료되었다면 아무것도 반환하지 않는다
        if (refreshTokenEntity == null || refreshTokenEntity.isExpired()) {
            if (refreshTokenEntity != null) {
                refreshTokenMapper.deleteByToken(refreshTokenEntity.getToken());
            }
            return new AccessTokenDto(null);
        }

        // 새로 access token 을 발급한다.
        var accessToken = generateToken(refreshTokenEntity.getEmail());
        Cookie cookie = new Cookie("Authorization", "Bearer+" + accessToken);
        cookie.setMaxAge(3600);
        cookie.setHttpOnly(true);
        cookie.setPath("/api");
        return new AccessTokenDto(accessToken);
    }
}
