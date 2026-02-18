package com.dkhien.springsecurityplayground.controller.jwt;

import com.dkhien.springsecurityplayground.api.jwt.JwtTokenApi;
import com.dkhien.springsecurityplayground.entity.RefreshToken;
import com.dkhien.springsecurityplayground.model.jwt.LoginRequest;
import com.dkhien.springsecurityplayground.model.jwt.LoginResponse;
import com.dkhien.springsecurityplayground.model.jwt.RefreshRequest;
import com.dkhien.springsecurityplayground.service.jwt.JwtTokenProvider;
import com.dkhien.springsecurityplayground.entity.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtTokenController implements JwtTokenApi {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Override
    public ResponseEntity<LoginResponse> createToken(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        SecurityUser user = (SecurityUser) authentication.getPrincipal();

        String accessToken = tokenProvider.generateAccessToken(user);
        String refreshToken = tokenProvider.generateRefreshToken(user);
        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
    }

    @Override
    public ResponseEntity<LoginResponse> refreshToken(RefreshRequest refreshRequest) {
        RefreshToken refreshToken = tokenProvider.validateRefreshToken(refreshRequest.getRefreshToken());

        SecurityUser user = SecurityUser.from(refreshToken.getAppUser());

        String newAccessToken = tokenProvider.generateAccessToken(user);
        String newRefreshToken = tokenProvider.generateRefreshToken(refreshToken.getAppUser());
        return ResponseEntity.ok(new LoginResponse(newAccessToken, newRefreshToken));
    }
}
