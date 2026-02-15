package com.dkhien.springsecurityplayground.controller.jwt;

import com.dkhien.springsecurityplayground.dto.LoginRequest;
import com.dkhien.springsecurityplayground.dto.LoginResponse;
import com.dkhien.springsecurityplayground.dto.RefreshRequest;
import com.dkhien.springsecurityplayground.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jwt/auth")
public class JwtAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthController(AuthenticationManager authenticationManager,
                             JwtTokenProvider tokenProvider,
                             UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken();
        return new LoginResponse(accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestBody RefreshRequest request) {
        if (!tokenProvider.validateRefreshToken(request.refreshToken())) {
            return ResponseEntity.status(401).build();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername("user");

        String newAccessToken = tokenProvider.generateAccessToken(userDetails);
        String newRefreshToken = tokenProvider.generateRefreshToken();
        return ResponseEntity.ok(new LoginResponse(newAccessToken, newRefreshToken));
    }
}
