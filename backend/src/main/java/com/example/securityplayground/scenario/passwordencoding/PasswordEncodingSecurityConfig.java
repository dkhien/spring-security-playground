package com.example.securityplayground.scenario.passwordencoding;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class PasswordEncodingSecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    public PasswordEncodingSecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    @Order(14)
    public SecurityFilterChain passwordEncodingFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/demo/password-encoding/**")
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }
}
