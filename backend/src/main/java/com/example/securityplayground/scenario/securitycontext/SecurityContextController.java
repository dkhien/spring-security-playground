package com.example.securityplayground.scenario.securitycontext;

import com.example.securityplayground.common.dto.ApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/demo/security-context")
public class SecurityContextController {

    @GetMapping("/inspect")
    public ApiResponse<Map<String, Object>> inspect() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();

        Map<String, Object> info = new LinkedHashMap<>();
        info.put("securityContextClass", context.getClass().getSimpleName());
        info.put("strategyName", SecurityContextHolder.getContextHolderStrategy().getClass().getSimpleName());

        if (auth != null) {
            info.put("authenticated", auth.isAuthenticated());
            info.put("principalType", auth.getPrincipal().getClass().getSimpleName());
            info.put("principalValue", auth.getPrincipal().toString());
            info.put("name", auth.getName());
            info.put("authoritiesCount", auth.getAuthorities().size());
            info.put("authorities", auth.getAuthorities().stream().map(a -> a.getAuthority()).toList());
            info.put("authenticationType", auth.getClass().getSimpleName());
            info.put("hasCredentials", auth.getCredentials() != null);
            info.put("detailsType", auth.getDetails() != null ? auth.getDetails().getClass().getSimpleName() : "null");
        } else {
            info.put("authenticated", false);
            info.put("message", "SecurityContext contains no Authentication object. Send credentials via Basic auth to see the difference.");
        }

        return ApiResponse.ok(info);
    }

    @GetMapping("/thread-info")
    public ApiResponse<Map<String, Object>> threadInfo() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();

        Map<String, Object> info = new LinkedHashMap<>();
        info.put("threadName", Thread.currentThread().getName());
        info.put("threadId", Thread.currentThread().getId());
        info.put("contextHolderStrategy", SecurityContextHolder.getContextHolderStrategy().getClass().getName());
        info.put("explanation", "SecurityContextHolder uses ThreadLocal by default - each thread has its own SecurityContext");

        if (auth != null) {
            info.put("authenticationPresent", true);
            info.put("username", auth.getName());
        } else {
            info.put("authenticationPresent", false);
        }

        return ApiResponse.ok(info);
    }
}
