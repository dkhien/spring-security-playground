package com.dkhien.springsecurityplayground.service.jwt;

import com.dkhien.springsecurityplayground.entity.RefreshToken;
import com.dkhien.springsecurityplayground.exception.RefreshTokenNotFoundException;
import com.dkhien.springsecurityplayground.repository.RefreshTokenRepository;
import com.dkhien.springsecurityplayground.utility.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRawToken(String rawToken) {
        return refreshTokenRepository.findByTokenHash(Utils.sha256(rawToken))
                .orElseThrow(RefreshTokenNotFoundException::new);
    }

    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken revokeRefreshToken(RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
        return save(refreshToken);
    }

    public void revokeAllByUser(Long appUserId) {
        var tokens = refreshTokenRepository.findAllByAppUserId(appUserId);
        tokens.forEach(token -> token.setRevoked(true));
        refreshTokenRepository.saveAll(tokens);
    }
}
