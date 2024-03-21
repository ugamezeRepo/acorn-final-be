package com.acorn.finals.service;

import com.acorn.finals.config.properties.TokenPropertiesConfig;
import com.acorn.finals.mapper.RefreshTokenMapper;
import com.acorn.finals.model.AcornJwt;
import com.acorn.finals.model.entity.RefreshTokenEntity;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {
    private final TokenPropertiesConfig tokenPropertiesConfig;
    private final RefreshTokenMapper refreshTokenMapper;
    private String secretString;
    private long expiration;

    @PostConstruct
    public void init() {
        secretString = tokenPropertiesConfig.getAccessToken().getSecret();
        expiration = tokenPropertiesConfig.getAccessToken().getExpiration();
    }

    /**
     * create access token from email , email data will be inserted into jwt subject field (sub) expiration time can be
     * configured in application-token.properties
     *
     * @param memberId
     * @return new access token jws
     */
    public String createAccessTokenFromMemberId(Integer memberId) {
        var currentMs = System.currentTimeMillis();
        var token = AcornJwt.builder()
                .subject(memberId.toString())
                .issuedAt(new Date(currentMs))
                .expirationTime(new Date(currentMs + 1000 * expiration))
                .key(secretString)
                .build();
        return token.toString();
    }

    /**
     * delete RefreshToken by Email when user logout
     *
     * @param memberId
     * @return delete RefreshToken
     */
    public boolean deleteRefreshTokenByMemberId(Integer memberId) {
        var isSuccess = false;
        int result = refreshTokenMapper.deleteByMemberId(memberId);
        if (result > 0) {
            isSuccess = true;
        }

        return isSuccess;
    }

    /**
     * create access token from refresh token,
     *
     * @param refreshToken
     * @return unique access token jws if refresh-token is valid else null
     */
    @Transactional
    public String createAccessTokenFromRefreshToken(String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenMapper.findOneTokenByToken(refreshToken);
        if (refreshTokenEntity == null || refreshTokenEntity.isExpired()) {
            if (refreshTokenEntity != null) {
                refreshTokenMapper.deleteByToken(refreshTokenEntity.getToken());
            }
            return null;
        }
        return createAccessTokenFromMemberId(refreshTokenEntity.getMemberId());
    }

    /**
     * create refresh token from email, create if not exists else update
     *
     * @param memberId
     * @return unique refresh token value
     */
    @Transactional
    public String createRefreshTokenFromMemberId(Integer memberId) {
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setMemberId(memberId);
        entity.setExpireDate(LocalDateTime.now().plusSeconds(tokenPropertiesConfig.getRefreshToken().getExpiration()));
        entity.setToken(UUID.randomUUID().toString());

        var existingToken = refreshTokenMapper.findOneTokenByMemberId(memberId);
        if (existingToken == null) {
            refreshTokenMapper.insert(entity);
        } else {
            entity.setId(existingToken.getId());
            refreshTokenMapper.update(entity);
        }
        return entity.getToken();
    }
}
