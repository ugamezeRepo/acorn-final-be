package com.acorn.finals.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class JwtService {
    public String generateJwsWithString(String payload) {
        String secretString = "long-longlonglonglonglonglonglonglong-long-long-long-long-long-secret";
        
        var encodedSecretString = Encoders.BASE64.encode(secretString.getBytes(StandardCharsets.UTF_8));
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(encodedSecretString));
        String secret = Encoders.BASE64.encode(key.getEncoded());
        log.debug(secret); // secret key here

        return Jwts.builder()
                .subject(payload)
                .signWith(key)
                .compact();
    }
}
