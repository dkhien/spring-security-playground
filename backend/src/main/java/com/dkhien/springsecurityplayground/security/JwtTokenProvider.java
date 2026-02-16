package com.dkhien.springsecurityplayground.security;

import com.dkhien.springsecurityplayground.entity.AppUser;
import com.dkhien.springsecurityplayground.exception.InvalidRefreshTokenException;
import com.dkhien.springsecurityplayground.service.AppUserService;
import com.dkhien.springsecurityplayground.service.RefreshTokenService;
import com.dkhien.springsecurityplayground.utility.Utils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import com.dkhien.springsecurityplayground.entity.RefreshToken;

import java.time.Instant;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Autowired
    private final RefreshTokenService refreshTokenService;

    @Autowired
    private final AppUserService appUserService;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateAccessToken(SecurityUser user) {
        return Jwts.builder()
                .id(user.getId().toString())
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(SecurityUser user) {
        return generateRefreshToken(appUserService.findByIdInternal(user.getId()));
    }

    public String generateRefreshToken(AppUser appUser) {
        String rawToken = UUID.randomUUID().toString();
        var refreshToken = new RefreshToken();
        refreshToken.setTokenHash(Utils.sha256(rawToken));
        refreshToken.setExpiryDate(Instant.ofEpochMilli(System.currentTimeMillis() + refreshTokenExpiration));
        refreshToken.setRevoked(false);
        refreshToken.setAppUser(appUser);
        refreshTokenService.save(refreshToken);
        return rawToken;
    }

    public Authentication parseAccessToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String username = claims.getSubject();
        var appUser = appUserService.findByUsernameInternal(username);
        SecurityUser user = SecurityUser.from(appUser);

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(accessToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    public RefreshToken validateRefreshToken(String rawToken) {
        var refreshToken = refreshTokenService.findByRawToken(rawToken);

        if (refreshToken.isRevoked()) {
            log.warn("Sign of theft: revoked refresh token being used. Revoking all refresh tokens of user.");
            refreshTokenService.revokeAllByUser(refreshToken.getAppUser().getId());
            throw new InvalidRefreshTokenException("revoked");
        }

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            log.warn("Expired refresh token");
            throw new InvalidRefreshTokenException("expired");
        }

        refreshTokenService.revokeRefreshToken(refreshToken);
        return refreshToken;
    }
}
