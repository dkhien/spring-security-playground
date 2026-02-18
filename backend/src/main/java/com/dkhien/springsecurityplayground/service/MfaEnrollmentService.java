package com.dkhien.springsecurityplayground.service;

import com.dkhien.springsecurityplayground.config.TotpConfig;
import com.dkhien.springsecurityplayground.entity.AppUser;
import com.dkhien.springsecurityplayground.exception.InvalidOtpException;
import com.dkhien.springsecurityplayground.security.SecretService;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Slf4j
@Service
@RequiredArgsConstructor
public class MfaEnrollmentService {

    public record MfaEnrollResult(String secret, String qrCodeUri) {}

    private final AppUserService appUserService;

    private final SecretService secretService;

    private final TotpConfig totpConfig;

    public MfaEnrollResult startEnrollment() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = appUserService.findByUsernameInternal(authentication.getName());

        if (Boolean.TRUE.equals(user.getMfaEnabled())) {
            throw new IllegalStateException("MFA is already enabled");
        }

        var secret = secretService.generateSecret();

        QrData data = new QrData.Builder()
                .label(user.getEmail())
                .secret(secret)
                .issuer(totpConfig.issuer())
                .algorithm(HashingAlgorithm.SHA1)
                .digits(totpConfig.digits())
                .period(totpConfig.period())
                .build();

        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData;

        try {
            imageData = generator.generate(data);
        } catch (QrGenerationException e) {
            log.error("Error generating TOTP QR for user {}", user.getUsername());
            throw new RuntimeException("Failed to generate QR code", e);
        }

        String mimeType = generator.getImageMimeType();
        String dataUri = getDataUriForImage(imageData, mimeType);

        user.setMfaSecret(secret);
        appUserService.saveUser(user);

        return new MfaEnrollResult(secret, dataUri);
    }

    public void confirmEnrollment(String code) {
        var user = getAuthenticatedAppUser();
        verifyOtpCode(user.getMfaSecret(), code);
        user.setMfaEnabled(true);
        appUserService.saveUser(user);
    }

    public void disableMfa(String code) {
        var user = getAuthenticatedAppUser();
        if (!Boolean.TRUE.equals(user.getMfaEnabled())) {
            throw new IllegalStateException("MFA is not enabled");
        }
        verifyOtpCode(user.getMfaSecret(), code);
        user.setMfaEnabled(false);
        user.setMfaSecret(null);
        appUserService.saveUser(user);
    }

    private AppUser getAuthenticatedAppUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return appUserService.findByUsernameInternal(authentication.getName());
    }

    private void verifyOtpCode(String secret, String code) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        if (!verifier.isValidCode(secret, code)) {
            throw new InvalidOtpException();
        }
    }
}
