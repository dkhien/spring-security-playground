package com.example.securityplayground.scenario.customfilter;

import com.example.securityplayground.common.dto.ApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/demo/custom-filter")
public class CustomFilterController {

    @GetMapping("/api-key")
    public ApiResponse<Map<String, Object>> apiKeyProtected(Authentication auth) {
        return ApiResponse.ok(Map.of(
                "message", "Authenticated via API Key filter",
                "principal", auth.getName(),
                "authorities", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                "authType", auth.getClass().getSimpleName()
        ));
    }

    @GetMapping("/rate-limited")
    public ApiResponse<String> rateLimited() {
        return ApiResponse.ok("Success! Check the X-RateLimit-* response headers.");
    }

    @GetMapping("/public")
    public ApiResponse<String> publicEndpoint() {
        return ApiResponse.ok("Public endpoint - no custom filter applied");
    }
}
