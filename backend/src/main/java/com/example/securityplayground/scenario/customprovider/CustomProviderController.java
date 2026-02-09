package com.example.securityplayground.scenario.customprovider;

import com.example.securityplayground.common.dto.ApiResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/demo/custom-provider")
public class CustomProviderController {

    private final AuthenticationManager authManager;

    public CustomProviderController(AuthenticationManager customProviderAuthManager) {
        this.authManager = customProviderAuthManager;
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String pin = request.get("pin");

        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, pin)
            );

            return ApiResponse.ok("PIN authentication successful", Map.of(
                    "username", auth.getName(),
                    "authorities", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                    "providerUsed", "PinAuthenticationProvider",
                    "explanation", "The custom AuthenticationProvider validated the PIN and created an Authentication object"
            ));
        } catch (AuthenticationException e) {
            return ApiResponse.error("Authentication failed: " + e.getMessage());
        }
    }

    @GetMapping("/protected")
    public ApiResponse<String> protectedResource(Authentication auth) {
        return ApiResponse.ok("Authenticated as " + auth.getName() + " via custom provider");
    }
}
