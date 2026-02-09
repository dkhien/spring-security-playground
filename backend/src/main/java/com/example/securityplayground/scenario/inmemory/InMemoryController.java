package com.example.securityplayground.scenario.inmemory;

import com.example.securityplayground.common.dto.ApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/demo/in-memory")
public class InMemoryController {

    @GetMapping("/protected")
    public ApiResponse<Map<String, Object>> protectedResource(Authentication authentication) {
        return ApiResponse.ok(Map.of(
                "message", "Authenticated with in-memory user: " + authentication.getName(),
                "authorities", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).toList()
        ));
    }

    @GetMapping("/users")
    public ApiResponse<List<Map<String, Object>>> listUsers() {
        return ApiResponse.ok("In-memory users configured in Java code", List.of(
                Map.of("username", "alice", "roles", List.of("USER"), "note", "password: alice123"),
                Map.of("username", "bob", "roles", List.of("ADMIN", "USER"), "note", "password: bob123")
        ));
    }
}
