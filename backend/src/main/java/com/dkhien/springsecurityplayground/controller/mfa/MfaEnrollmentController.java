package com.dkhien.springsecurityplayground.controller.mfa;

import com.dkhien.springsecurityplayground.api.mfa.MfaEnrollmentApi;
import com.dkhien.springsecurityplayground.model.mfa.ConfirmEnrollRequest;
import com.dkhien.springsecurityplayground.model.mfa.DisableMfaRequest;
import com.dkhien.springsecurityplayground.model.mfa.MfaEnrollResponse;
import com.dkhien.springsecurityplayground.service.MfaEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MfaEnrollmentController implements MfaEnrollmentApi {

    private final MfaEnrollmentService mfaEnrollmentService;

    @Override
    public ResponseEntity<String> confirmMfaEnroll(ConfirmEnrollRequest confirmEnrollRequest) {
        mfaEnrollmentService.confirmEnrollment(confirmEnrollRequest.getOtpCode());
        return ResponseEntity.ok("MFA enabled successfully");
    }

    @Override
    public ResponseEntity<String> disableMfa(DisableMfaRequest disableMfaRequest) {
        mfaEnrollmentService.disableMfa(disableMfaRequest.getOtpCode());
        return ResponseEntity.ok("MFA disabled successfully");
    }

    @Override
    public ResponseEntity<MfaEnrollResponse> enrollMfa() {
        var enrollResult = mfaEnrollmentService.startEnrollment();
        return ResponseEntity.ok(new MfaEnrollResponse(enrollResult.secret(), enrollResult.qrCodeUri()));
    }
}
