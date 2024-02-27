package com.acorn.finals.model.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenEntityTest {

    @Test
    void isExpired() {
        // 만료 된 토큰 테스트
        var expiredRefreshToken = new RefreshTokenEntity();
        expiredRefreshToken.setExpireDate(LocalDateTime.now().minusDays(1));
        assertThat(expiredRefreshToken.isExpired()).isEqualTo(true);


        // 만료 안된거 테스트
        var unexpiredRefreshToken = new RefreshTokenEntity();
        unexpiredRefreshToken.setExpireDate(LocalDateTime.now().plusDays(3));
        assertThat(unexpiredRefreshToken.isExpired()).isEqualTo(false);
    }
}