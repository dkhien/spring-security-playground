package com.dkhien.springsecurityplayground.controller.jwt;

import com.dkhien.springsecurityplayground.api.jwt.JwtTokenApi;
import com.dkhien.springsecurityplayground.model.jwt.LoginRequest;
import com.dkhien.springsecurityplayground.model.jwt.LoginResponse;
import com.dkhien.springsecurityplayground.model.jwt.RefreshRequest;
import com.dkhien.springsecurityplayground.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtTokenController implements JwtTokenApi {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtTokenController(AuthenticationManager authenticationManager,
                              JwtTokenProvider tokenProvider,
                              UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public ResponseEntity<LoginResponse> createToken(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken();
        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
    }

    @Override
    public ResponseEntity<LoginResponse> refreshToken(RefreshRequest refreshRequest) {
        if (!tokenProvider.validateRefreshToken(refreshRequest.getRefreshToken())) {
            return ResponseEntity.status(401).build();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername("user");

        String newAccessToken = tokenProvider.generateAccessToken(userDetails);
        String newRefreshToken = tokenProvider.generateRefreshToken();
        return ResponseEntity.ok(new LoginResponse(newAccessToken, newRefreshToken));
    }
}
