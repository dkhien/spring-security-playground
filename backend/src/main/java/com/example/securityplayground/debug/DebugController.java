package com.example.securityplayground.debug;

import com.example.securityplayground.common.dto.ApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    private final FilterChainDiagnosticService diagnosticService;

    public DebugController(FilterChainDiagnosticService diagnosticService) {
        this.diagnosticService = diagnosticService;
    }

    @GetMapping("/filter-chains")
    public ApiResponse<List<Map<String, Object>>> getAllFilterChains() {
        return ApiResponse.ok(diagnosticService.getAllFilterChains());
    }

    @GetMapping("/filter-chain")
    public ApiResponse<Map<String, Object>> getFilterChainForPath(@RequestParam String path) {
        return ApiResponse.ok(diagnosticService.getFilterChainForPath(path));
    }

    @GetMapping("/security-context")
    public ApiResponse<Map<String, Object>> getSecurityContext() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> info = new LinkedHashMap<>();

        if (auth != null) {
            info.put("authenticated", auth.isAuthenticated());
            info.put("principal", auth.getPrincipal().toString());
            info.put("name", auth.getName());
            info.put("authorities", auth.getAuthorities().stream()
                    .map(a -> a.getAuthority()).toList());
            info.put("credentialsType", auth.getCredentials() != null ? auth.getCredentials().getClass().getSimpleName() : "null");
            info.put("authType", auth.getClass().getSimpleName());
        } else {
            info.put("authenticated", false);
            info.put("message", "No authentication in SecurityContext");
        }

        return ApiResponse.ok(info);
    }
}
