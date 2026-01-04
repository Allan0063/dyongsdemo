package com.dyongs.demo.global.jwt;

import com.dyongs.demo.domain.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    // Access Token 유효 시간 (예: 30분)
    private final long accessTokenValidityMs = 30 * 60 * 1000L;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void initKeys() {
        try {
            // resources/keys/private.pem, public.pem 사용
            this.privateKey = loadPrivateKey("keys/private.pem");
            this.publicKey = loadPublicKey("keys/public.pem");
            log.info("RSA keys loaded successfully");
        } catch (Exception e) {
            log.error("Failed to load RSA keys", e);
            throw new IllegalStateException("Cannot load RSA keys", e);
        }
    }

    private PrivateKey loadPrivateKey(String path) throws Exception {
        String keyPem = readPemFile(path);
        // -----BEGIN PRIVATE KEY----- / -----END PRIVATE KEY----- 제거
        String base64Key = keyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Decoders.BASE64.decode(base64Key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    private PublicKey loadPublicKey(String path) throws Exception {
        String keyPem = readPemFile(path);
        // -----BEGIN PUBLIC KEY----- / -----END PUBLIC KEY----- 제거
        String base64Key = keyPem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Decoders.BASE64.decode(base64Key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    private String readPemFile(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        try (var is = resource.getInputStream()) {
            String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return text;
        }
    }

    // Access Token 생성
    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(accessTokenValidityMs);

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    // 토큰에서 userId 추출
    public Long getUserId(String token) {
        Claims claims = parseClaims(token);
        return Long.valueOf(claims.getSubject());
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT expired", e);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT", e);
        }
        return false;
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)  // RS256 검증은 public key 사용
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
