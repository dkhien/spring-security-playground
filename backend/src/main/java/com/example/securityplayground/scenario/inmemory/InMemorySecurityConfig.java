package com.example.securityplayground.scenario.inmemory;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Map;

@Configuration
public class InMemorySecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    public InMemorySecurityConfig(CorsConfigurationSource corsConfigurationSource,
                                  ObjectMapper objectMapper,
                                  PasswordEncoder passwordEncoder) {
        this.corsConfigurationSource = corsConfigurationSource;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.builder()
                        .username("alice")
                        .password(passwordEncoder.encode("alice123"))
                        .roles("USER")
                        .build(),
                User.builder()
                        .username("bob")
                        .password(passwordEncoder.encode("bob123"))
                        .roles("ADMIN", "USER")
                        .build()
        );
    }

    @Bean
    @Order(12)
    public SecurityFilterChain inMemoryFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/demo/in-memory/**")
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(inMemoryUserDetailsManager())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/demo/in-memory/users").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> basic
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write(objectMapper.writeValueAsString(
                                    Map.of("success", false, "message", "Authentication required (use in-memory users: alice or bob)")
                            ));
                        })
                );

        return http.build();
    }
}
