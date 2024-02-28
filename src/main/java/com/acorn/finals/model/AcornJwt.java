package com.acorn.finals.model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * jwt spec from rfc7519
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AcornJwt {
    private String issuer;
    private String subject;
    private Set<String> audience;
    private Date expirationTime;
    private Date notBefore;
    private Date issuedAt;
    private String jwtId;

    // signing key
    private String key;

    /**
     * @param jws
     * @param key
     * @return null if jws is not valid, else jwt token
     */
    public static AcornJwt fromJws(String jws, String key) {
        var hmacKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        JwtParser parser = Jwts.parser().verifyWith(hmacKey).build();
        Claims claims;
        try {
            claims = parser.parseSignedClaims(jws).getPayload();
        } catch (Exception e) {
            return null;
        }
        return AcornJwt.builder()
                .issuer(claims.getIssuer())
                .subject(claims.getSubject())
                .audience(claims.getAudience())
                .expirationTime(claims.getExpiration())
                .notBefore(claims.getNotBefore())
                .issuedAt(claims.getIssuedAt())
                .jwtId(claims.getId())
                .build();
    }

    @Override
    public String toString() {
        var hmacKey = key == null ? null : Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        var builder = Jwts.builder();
        if (audience != null) {
            for (var aud : audience) {
                builder.audience().add(aud);
            }
        }
        return builder.issuer(issuer)
                .subject(subject)
                .expiration(expirationTime)
                .notBefore(notBefore)
                .issuedAt(issuedAt)
                .id(jwtId)
                .signWith(hmacKey)
                .compact();
    }

    public boolean isExpired() {
        if (expirationTime == null) {
            return false;
        }
        return expirationTime.before(new Date());
    }
}
