package com.example.securityplayground.scenario.jwt;

import com.example.securityplayground.common.dto.ApiResponse;
import com.example.securityplayground.common.service.AppUserService;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/demo/jwt")
public class JwtController {

    private final AppUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public JwtController(AppUserService userService, PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        UserDetails userDetails = userService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String accessToken = jwtTokenService.generateAccessToken(userDetails);
        String refreshToken = jwtTokenService.generateRefreshToken(userDetails);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("accessToken", accessToken);
        data.put("refreshToken", refreshToken);
        data.put("tokenType", "Bearer");
        data.put("accessTokenExpiresIn", jwtTokenService.getAccessTokenExpirySeconds());
        data.put("refreshTokenExpiresIn", jwtTokenService.getRefreshTokenExpirySeconds());

        return ApiResponse.ok("Login successful", data);
    }

    @PostMapping("/refresh")
    public ApiResponse<Map<String, Object>> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (!jwtTokenService.isTokenValid(refreshToken)) {
            return ApiResponse.error("Invalid or expired refresh token");
        }

        Claims claims = jwtTokenService.parseToken(refreshToken);
        if (!"refresh".equals(claims.get("type", String.class))) {
            return ApiResponse.error("Not a refresh token");
        }

        String username = claims.getSubject();
        UserDetails userDetails = userService.loadUserByUsername(username);
        String newAccessToken = jwtTokenService.generateAccessToken(userDetails);

        return ApiResponse.ok("Token refreshed", Map.of(
                "accessToken", newAccessToken,
                "tokenType", "Bearer",
                "expiresIn", jwtTokenService.getAccessTokenExpirySeconds()
        ));
    }

    @GetMapping("/protected")
    public ApiResponse<String> protectedResource(Authentication authentication) {
        return ApiResponse.ok("Hello, " + authentication.getName() + "! You accessed this resource with a valid JWT.");
    }

    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> me(Authentication authentication) {
        return ApiResponse.ok(Map.of(
                "username", authentication.getName(),
                "authorities", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).toList()
        ));
    }

    @PostMapping("/decode")
    public ApiResponse<Map<String, Object>> decode(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return ApiResponse.error("Invalid JWT format - expected 3 parts separated by dots");
            }

            String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("header", headerJson);
            result.put("payload", payloadJson);
            result.put("signature", parts[2]);

            // Try to verify
            try {
                jwtTokenService.parseToken(token);
                result.put("valid", true);
                result.put("verificationMessage", "Signature is valid");
            } catch (Exception e) {
                result.put("valid", false);
                result.put("verificationMessage", "Signature verification failed: " + e.getMessage());
            }

            return ApiResponse.ok(result);
        } catch (Exception e) {
            return ApiResponse.error("Failed to decode token: " + e.getMessage());
        }
    }
}
