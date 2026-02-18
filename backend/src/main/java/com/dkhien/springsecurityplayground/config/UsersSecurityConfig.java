package com.dkhien.springsecurityplayground.config;

import com.dkhien.springsecurityplayground.service.jwt.JwtAuthenticationFilter;
import com.dkhien.springsecurityplayground.entity.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class UsersSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public UsersSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    @Order(4)
    SecurityFilterChain usersFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/users/**")
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}/role").hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
