package com.acorn.finals.service;

import io.jsonwebtoken.JwtException;
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
    private static final String secretString = "long-longlonglonglonglonglonglonglong-long-long-long-long-long-secret";

    private static final String encodedSecretString = Encoders.BASE64.encode(secretString.getBytes(StandardCharsets.UTF_8));
    SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(encodedSecretString));
    String secret = Encoders.BASE64.encode(key.getEncoded());
    public String generateJwsWithString(String payload) {
        

        log.debug(secret); // secret key here

        return Jwts.builder()
                .subject(payload)
                .signWith(key)
                .compact();
    }
    public boolean verifyUserName(String jwt, String userName) {
        boolean claim;
        try {

            var parsedJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt);

            claim = parsedJws.getPayload().getSubject().equals(userName);
            //OK, we can trust this JWT

        } catch (JwtException e) {

            claim = false;
        }
        return claim;
    }

}
