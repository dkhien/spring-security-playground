package com.dkhien.springsecurityplayground.service;

import com.dkhien.springsecurityplayground.entity.RefreshToken;
import com.dkhien.springsecurityplayground.repository.RefreshTokenRepository;
import com.dkhien.springsecurityplayground.utility.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findByRawToken(String rawToken) {
        return refreshTokenRepository.findByTokenHash(Utils.sha256(rawToken));
    }

    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken revokeAccessToken(RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
        return save(refreshToken);
    }
}
