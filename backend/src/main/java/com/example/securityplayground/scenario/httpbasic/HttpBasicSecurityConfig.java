package com.example.securityplayground.scenario.httpbasic;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Map;

@Configuration
public class HttpBasicSecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final ObjectMapper objectMapper;

    public HttpBasicSecurityConfig(CorsConfigurationSource corsConfigurationSource, ObjectMapper objectMapper) {
        this.corsConfigurationSource = corsConfigurationSource;
        this.objectMapper = objectMapper;
    }

    @Bean
    @Order(11)
    public SecurityFilterChain httpBasicFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/demo/http-basic/**")
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/demo/http-basic/public").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> basic
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.setHeader("WWW-Authenticate", "Basic realm=\"HTTP Basic Demo\"");
                            response.getWriter().write(objectMapper.writeValueAsString(
                                    Map.of("success", false, "message", "HTTP Basic authentication required")
                            ));
                        })
                );

        return http.build();
    }
}
