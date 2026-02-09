package com.example.securityplayground.scenario.securitycontext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityContextSecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityContextSecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    @Order(22)
    public SecurityFilterChain securityContextFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/demo/security-context/**")
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .httpBasic(basic -> {});

        return http.build();
    }
}
