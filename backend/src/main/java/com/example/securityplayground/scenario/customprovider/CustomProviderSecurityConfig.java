package com.example.securityplayground.scenario.customprovider;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;
import java.util.Map;

@Configuration
public class CustomProviderSecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final ObjectMapper objectMapper;

    public CustomProviderSecurityConfig(CorsConfigurationSource corsConfigurationSource, ObjectMapper objectMapper) {
        this.corsConfigurationSource = corsConfigurationSource;
        this.objectMapper = objectMapper;
    }

    @Bean
    public PinAuthenticationProvider pinAuthenticationProvider() {
        return new PinAuthenticationProvider();
    }

    @Bean
    public AuthenticationManager customProviderAuthManager() {
        return new ProviderManager(List.of(pinAuthenticationProvider()));
    }

    @Bean
    @Order(26)
    public SecurityFilterChain customProviderFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/demo/custom-provider/**")
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/demo/custom-provider/login").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> basic
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write(objectMapper.writeValueAsString(
                                    Map.of("success", false, "message", "Authentication required - use PIN-based auth via /login")
                            ));
                        })
                );

        return http.build();
    }
}
