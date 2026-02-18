package com.dkhien.springsecurityplayground.security;

import dev.samstevens.totp.secret.SecretGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecretService {
    private final SecretGenerator secretGenerator;

    public String generateSecret() {
        return secretGenerator.generate();
    }
}
