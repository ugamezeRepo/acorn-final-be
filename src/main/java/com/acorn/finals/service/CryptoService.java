package com.acorn.finals.service;

import org.bouncycastle.crypto.RuntimeCryptoException;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class CryptoService {
    public String encode(int plain) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(Integer.toString(plain).getBytes(StandardCharsets.UTF_8));
            return new String(Hex.encode(hash)).substring(0, 24);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeCryptoException();
        }
    }
}
