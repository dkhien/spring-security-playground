package com.example.securityplayground.scenario.rolebased;

import com.example.securityplayground.common.dto.ApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/demo/role-based")
public class RoleBasedController {

    @GetMapping("/public")
    public ApiResponse<String> publicEndpoint() {
        return ApiResponse.ok("Public endpoint - no role required");
    }

    @GetMapping("/user")
    public ApiResponse<Map<String, Object>> userEndpoint(Authentication auth) {
        return ApiResponse.ok(Map.of(
                "message", "Welcome, " + auth.getName() + "! You have the USER role.",
                "requiredRole", "ROLE_USER",
                "yourRoles", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        ));
    }

    @GetMapping("/manager")
    public ApiResponse<Map<String, Object>> managerEndpoint(Authentication auth) {
        return ApiResponse.ok(Map.of(
                "message", "Welcome, " + auth.getName() + "! You have the MANAGER role.",
                "requiredRole", "ROLE_MANAGER",
                "yourRoles", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        ));
    }

    @GetMapping("/admin")
    public ApiResponse<Map<String, Object>> adminEndpoint(Authentication auth) {
        return ApiResponse.ok(Map.of(
                "message", "Welcome, " + auth.getName() + "! You have the ADMIN role.",
                "requiredRole", "ROLE_ADMIN",
                "yourRoles", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        ));
    }
}
