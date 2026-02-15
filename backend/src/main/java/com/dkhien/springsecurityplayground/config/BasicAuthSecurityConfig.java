package com.dkhien.springsecurityplayground.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class BasicAuthSecurityConfig {

    @Bean
    @Order(1)
    SecurityFilterChain basicAuthFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/basic-auth/**")
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/basic-auth/public").permitAll()
                        .requestMatchers("/api/basic-auth/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
