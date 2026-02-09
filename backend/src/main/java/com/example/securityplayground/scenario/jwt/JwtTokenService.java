package com.example.securityplayground.scenario.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtTokenService {

    private static final String SECRET = "spring-security-playground-jwt-secret-key-must-be-at-least-256-bits-long";
    private static final long ACCESS_TOKEN_EXPIRY_SECONDS = 300; // 5 minutes
    private static final long REFRESH_TOKEN_EXPIRY_SECONDS = 86400; // 24 hours

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public String generateAccessToken(UserDetails userDetails) {
        Instant now = Instant.now();
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", roles)
                .claim("type", "access")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(ACCESS_TOKEN_EXPIRY_SECONDS)))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("type", "refresh")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(REFRESH_TOKEN_EXPIRY_SECONDS)))
                .signWith(key)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Claims decodeWithoutVerification(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT format");
        }
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public long getAccessTokenExpirySeconds() {
        return ACCESS_TOKEN_EXPIRY_SECONDS;
    }

    public long getRefreshTokenExpirySeconds() {
        return REFRESH_TOKEN_EXPIRY_SECONDS;
    }
}
