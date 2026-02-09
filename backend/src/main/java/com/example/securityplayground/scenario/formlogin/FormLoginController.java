package com.example.securityplayground.scenario.formlogin;

import com.example.securityplayground.common.dto.ApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/demo/form-login")
public class FormLoginController {

    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> me(Authentication authentication) {
        return ApiResponse.ok(Map.of(
                "username", authentication.getName(),
                "authorities", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).toList(),
                "authenticated", authentication.isAuthenticated()
        ));
    }

    @GetMapping("/public")
    public ApiResponse<String> publicEndpoint() {
        return ApiResponse.ok("This endpoint is accessible without authentication");
    }
}
