package com.example.securityplayground.scenario.session;

import com.example.securityplayground.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/demo/session")
public class SessionController {

    @GetMapping("/info")
    public ApiResponse<Map<String, Object>> sessionInfo(HttpServletRequest request, Authentication authentication) {
        HttpSession session = request.getSession(false);

        Map<String, Object> info = new LinkedHashMap<>();
        info.put("username", authentication.getName());

        if (session != null) {
            info.put("sessionId", session.getId());
            info.put("creationTime", Instant.ofEpochMilli(session.getCreationTime()).toString());
            info.put("lastAccessedTime", Instant.ofEpochMilli(session.getLastAccessedTime()).toString());
            info.put("maxInactiveIntervalSeconds", session.getMaxInactiveInterval());
            info.put("isNew", session.isNew());
        } else {
            info.put("session", "No session exists");
        }

        return ApiResponse.ok(info);
    }

    @GetMapping("/public")
    public ApiResponse<Map<String, Object>> publicEndpoint(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return ApiResponse.ok(Map.of(
                "message", "Public endpoint",
                "hasSession", session != null,
                "sessionId", session != null ? session.getId() : "none"
        ));
    }
}
