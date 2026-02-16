package com.dkhien.springsecurityplayground.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AuthSecurityConfig {

    @Bean
    @Order(0)
    SecurityFilterChain authFilterChain(HttpSecurity http) {
        http.securityMatcher("/api/auth/**")
                .authorizeHttpRequests(requests -> requests
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
