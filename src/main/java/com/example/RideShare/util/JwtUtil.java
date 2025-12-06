package com.example.RideShare.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long expirationMs;

    public JwtUtil(@Value("${app.jwt.secret:}") String secret,
                   @Value("${app.jwt.expirationMs:3600000}") long expirationMs) {

        this.expirationMs = expirationMs;

        SecretKey resolved = null;

        if (secret == null || secret.isBlank()) {
            resolved = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            System.out.println("[WARN] No JWT secret configured, generated ephemeral key (dev/test only).");
        } else {
            try {
                byte[] keyBytes;
                if (secret.startsWith("base64:")) {
                    String b64 = secret.substring("base64:".length());
                    keyBytes = Base64.getDecoder().decode(b64);
                } else {
                    keyBytes = secret.getBytes(java.nio.charset.StandardCharsets.UTF_8);

                }


                resolved = Keys.hmacShaKeyFor(keyBytes);

            } catch (IllegalArgumentException | io.jsonwebtoken.security.WeakKeyException ex) {
                // Provided secret was invalid or too weak. Fall back to generated secure key.
                resolved = Keys.secretKeyFor(SignatureAlgorithm.HS256);
                System.out.println("[WARN] Provided JWT secret is too weak or invalid. Generated ephemeral key (dev/test only).");
            } catch (Exception ex) {
                resolved = Keys.secretKeyFor(SignatureAlgorithm.HS256);
                System.out.println("[WARN] Error parsing JWT secret; generated ephemeral key (dev/test only).");
            }
        }

        this.key = resolved;
    }

    public String generateToken(String username, String role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String extractRole(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        Object r = claims.get("role");
        return r != null ? r.toString() : null;
    }
}
