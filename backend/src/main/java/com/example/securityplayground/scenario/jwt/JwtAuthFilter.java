package com.example.securityplayground.scenario.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;

    public JwtAuthFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                if (jwtTokenService.isTokenValid(token)) {
                    var claims = jwtTokenService.parseToken(token);
                    String username = claims.getSubject();
                    String roles = claims.get("roles", String.class);

                    var authorities = Arrays.stream(roles.split(","))
                            .filter(r -> !r.isBlank())
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                    var authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception ignored) {
                // Invalid token - continue without authentication
            }
        }

        filterChain.doFilter(request, response);
    }
}
