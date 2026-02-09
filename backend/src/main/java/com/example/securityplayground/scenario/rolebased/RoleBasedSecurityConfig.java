package com.example.securityplayground.scenario.rolebased;

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
public class RoleBasedSecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final ObjectMapper objectMapper;

    public RoleBasedSecurityConfig(CorsConfigurationSource corsConfigurationSource, ObjectMapper objectMapper) {
        this.corsConfigurationSource = corsConfigurationSource;
        this.objectMapper = objectMapper;
    }

    @Bean
    @Order(16)
    public SecurityFilterChain roleBasedFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/demo/role-based/**")
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/demo/role-based/public").permitAll()
                        .requestMatchers("/api/demo/role-based/admin").hasRole("ADMIN")
                        .requestMatchers("/api/demo/role-based/manager").hasRole("MANAGER")
                        .requestMatchers("/api/demo/role-based/user").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> basic
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write(objectMapper.writeValueAsString(
                                    Map.of("success", false, "message", "Authentication required")
                            ));
                        })
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write(objectMapper.writeValueAsString(
                                    Map.of("success", false, "message", "Access denied - insufficient role. " + accessDeniedException.getMessage())
                            ));
                        })
                );

        return http.build();
    }
}
