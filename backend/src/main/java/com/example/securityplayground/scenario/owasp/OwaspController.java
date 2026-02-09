package com.example.securityplayground.scenario.owasp;

import com.example.securityplayground.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/demo/owasp")
public class OwaspController {

    @GetMapping("/headers")
    public ApiResponse<Map<String, Object>> securityHeaders(HttpServletResponse response) {
        // The security headers are automatically added by Spring Security's headers() config.
        // We return an explanation of what headers were set.
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("message", "Check the response headers to see security headers added by Spring Security");
        info.put("expectedHeaders", Map.of(
                "X-Content-Type-Options", "nosniff - Prevents MIME type sniffing",
                "X-Frame-Options", "DENY - Prevents clickjacking by disabling iframe embedding",
                "Strict-Transport-Security", "max-age=31536000; includeSubDomains - Forces HTTPS",
                "Referrer-Policy", "strict-origin-when-cross-origin - Controls referrer information",
                "Content-Security-Policy", "default-src 'self' - Restricts resource loading sources"
        ));
        return ApiResponse.ok(info);
    }

    @GetMapping("/cors-info")
    public ApiResponse<Map<String, Object>> corsInfo() {
        return ApiResponse.ok(Map.of(
                "message", "CORS (Cross-Origin Resource Sharing) configuration",
                "configuration", Map.of(
                        "allowedOrigins", "http://localhost:5173, http://localhost:3000",
                        "allowedMethods", "GET, POST, PUT, DELETE, OPTIONS",
                        "allowCredentials", true,
                        "exposedHeaders", "Authorization, X-Auth-Token"
                ),
                "explanation", "CORS controls which origins can make requests to this API. "
                        + "Without proper CORS configuration, browsers block cross-origin requests."
        ));
    }

    @GetMapping("/csrf-info")
    public ApiResponse<Map<String, Object>> csrfInfo() {
        return ApiResponse.ok(Map.of(
                "message", "CSRF (Cross-Site Request Forgery) protection",
                "status", "Disabled for API demos (typical for stateless/JWT APIs)",
                "explanation", "CSRF protection is important for session-based auth with browser forms. "
                        + "For stateless APIs using JWT tokens, CSRF is typically disabled because "
                        + "the token itself serves as proof that the request came from an authorized source.",
                "whenToEnable", "Enable CSRF for: form-based login, session-based auth, server-rendered pages"
        ));
    }
}
