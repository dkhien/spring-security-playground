package com.dkhien.springsecurityplayground.config;

import com.dkhien.springsecurityplayground.security.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class UsersSecurityConfig {

    @Bean
    @Order(4)
    SecurityFilterChain usersFilterChain(HttpSecurity http) {
        http.securityMatcher("/api/users/**")
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}/role").hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
