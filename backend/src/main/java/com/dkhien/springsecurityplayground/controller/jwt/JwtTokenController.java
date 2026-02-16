package com.dkhien.springsecurityplayground.controller.jwt;

import com.dkhien.springsecurityplayground.api.jwt.JwtTokenApi;
import com.dkhien.springsecurityplayground.entity.RefreshToken;
import com.dkhien.springsecurityplayground.model.jwt.LoginRequest;
import com.dkhien.springsecurityplayground.model.jwt.LoginResponse;
import com.dkhien.springsecurityplayground.model.jwt.RefreshRequest;
import com.dkhien.springsecurityplayground.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtTokenController implements JwtTokenApi {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    public ResponseEntity<LoginResponse> createToken(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(userDetails);
        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
    }

    @Override
    public ResponseEntity<LoginResponse> refreshToken(RefreshRequest refreshRequest) {
        RefreshToken refreshToken = tokenProvider.validateRefreshToken(refreshRequest.getRefreshToken())
                .orElse(null);

        if (refreshToken == null) {
            return ResponseEntity.status(401).build();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(
                refreshToken.getAppUser().getUsername());

        String newAccessToken = tokenProvider.generateAccessToken(userDetails);
        String newRefreshToken = tokenProvider.generateRefreshToken(userDetails);
        return ResponseEntity.ok(new LoginResponse(newAccessToken, newRefreshToken));
    }
}
