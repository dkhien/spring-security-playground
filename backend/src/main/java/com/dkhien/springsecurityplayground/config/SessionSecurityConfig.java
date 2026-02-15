package com.dkhien.springsecurityplayground.config;

import com.dkhien.springsecurityplayground.security.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SessionSecurityConfig {

    @Bean
    @Order(2)
    SecurityFilterChain sessionFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/session/**")
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/session/public", "/api/session/login").permitAll()
                        .requestMatchers("/api/session/admin").hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/api/session/login")
                        .defaultSuccessUrl("/api/session/user", false)
                )
                .logout(logout -> logout
                        .logoutUrl("/api/session/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(200);
                            response.getWriter().write("Logged out successfully");
                        })
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
