package com.example.securityplayground.scenario.customfilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private static final Map<String, String> VALID_API_KEYS = Map.of(
            "demo-api-key-12345", "demo-user",
            "admin-api-key-99999", "admin-user"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String apiKey = request.getHeader("X-API-Key");

        if (apiKey != null && VALID_API_KEYS.containsKey(apiKey)) {
            String user = VALID_API_KEYS.get(apiKey);
            var auth = new UsernamePasswordAuthenticationToken(
                    user, null, List.of(new SimpleGrantedAuthority("ROLE_API_USER"))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
