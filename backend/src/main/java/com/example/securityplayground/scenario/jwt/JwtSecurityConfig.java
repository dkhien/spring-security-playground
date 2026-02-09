package com.example.securityplayground.scenario.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Map;

@Configuration
public class JwtSecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final ObjectMapper objectMapper;
    private final JwtTokenService jwtTokenService;

    public JwtSecurityConfig(CorsConfigurationSource corsConfigurationSource,
                             ObjectMapper objectMapper,
                             JwtTokenService jwtTokenService) {
        this.corsConfigurationSource = corsConfigurationSource;
        this.objectMapper = objectMapper;
        this.jwtTokenService = jwtTokenService;
    }

    @Bean
    @Order(20)
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/demo/jwt/**")
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthFilter(jwtTokenService), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/demo/jwt/login", "/api/demo/jwt/refresh", "/api/demo/jwt/decode").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write(objectMapper.writeValueAsString(
                                    Map.of("success", false, "message", "JWT authentication required. Provide a Bearer token in the Authorization header.")
                            ));
                        })
                );

        return http.build();
    }
}
