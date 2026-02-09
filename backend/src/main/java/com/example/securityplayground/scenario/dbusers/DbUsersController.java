package com.example.securityplayground.scenario.dbusers;

import com.example.securityplayground.common.dto.ApiResponse;
import com.example.securityplayground.common.entity.AppUser;
import com.example.securityplayground.common.repository.AppUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/demo/db-users")
public class DbUsersController {

    private final AppUserRepository userRepository;

    public DbUsersController(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/protected")
    public ApiResponse<String> protectedResource(Authentication authentication) {
        return ApiResponse.ok("Hello, " + authentication.getName() + "! Authenticated via database-backed UserDetailsService.");
    }

    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> me(Authentication authentication) {
        return ApiResponse.ok(Map.of(
                "username", authentication.getName(),
                "authorities", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).toList()
        ));
    }

    @GetMapping("/users")
    public ApiResponse<List<Map<String, Object>>> listUsers() {
        List<Map<String, Object>> users = userRepository.findAll().stream()
                .map(user -> Map.<String, Object>of(
                        "username", user.getUsername(),
                        "email", user.getEmail() != null ? user.getEmail() : "",
                        "enabled", user.isEnabled(),
                        "accountLocked", user.isAccountLocked(),
                        "roles", user.getRoles().stream().map(r -> r.getName()).toList()
                ))
                .toList();
        return ApiResponse.ok("Users from database", users);
    }
}
