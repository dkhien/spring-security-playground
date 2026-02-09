package com.example.securityplayground.scenario.methodsecurity;

import com.example.securityplayground.common.dto.ApiResponse;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/demo/method-security")
public class MethodSecurityController {

    @GetMapping("/pre-authorize-user")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<Map<String, Object>> preAuthorizeUser(Authentication auth) {
        return ApiResponse.ok(Map.of(
                "message", "Access granted by @PreAuthorize(\"hasRole('USER')\")",
                "annotation", "@PreAuthorize(\"hasRole('USER')\")",
                "username", auth.getName(),
                "roles", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        ));
    }

    @GetMapping("/pre-authorize-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Map<String, Object>> preAuthorizeAdmin(Authentication auth) {
        return ApiResponse.ok(Map.of(
                "message", "Access granted by @PreAuthorize(\"hasRole('ADMIN')\")",
                "annotation", "@PreAuthorize(\"hasRole('ADMIN')\")",
                "username", auth.getName(),
                "roles", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        ));
    }

    @GetMapping("/secured-manager")
    @Secured("ROLE_MANAGER")
    public ApiResponse<Map<String, Object>> securedManager(Authentication auth) {
        return ApiResponse.ok(Map.of(
                "message", "Access granted by @Secured(\"ROLE_MANAGER\")",
                "annotation", "@Secured(\"ROLE_MANAGER\")",
                "username", auth.getName(),
                "roles", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        ));
    }

    @GetMapping("/pre-authorize-spel")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #auth.name == 'user')")
    public ApiResponse<Map<String, Object>> preAuthorizeSpel(Authentication auth) {
        return ApiResponse.ok(Map.of(
                "message", "Access granted by SpEL expression",
                "annotation", "@PreAuthorize(\"hasRole('ADMIN') or (hasRole('USER') and #auth.name == 'user')\")",
                "explanation", "Admins always have access; USER role only if username is 'user'",
                "username", auth.getName(),
                "roles", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        ));
    }
}
