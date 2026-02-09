package com.example.securityplayground.scenario.dbusers;

import com.example.securityplayground.common.service.AppUserService;
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
public class DbUsersSecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final ObjectMapper objectMapper;
    private final AppUserService appUserService;

    public DbUsersSecurityConfig(CorsConfigurationSource corsConfigurationSource,
                                 ObjectMapper objectMapper,
                                 AppUserService appUserService) {
        this.corsConfigurationSource = corsConfigurationSource;
        this.objectMapper = objectMapper;
        this.appUserService = appUserService;
    }

    @Bean
    @Order(13)
    public SecurityFilterChain dbUsersFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/demo/db-users/**")
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(appUserService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/demo/db-users/users").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> basic
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write(objectMapper.writeValueAsString(
                                    Map.of("success", false, "message", "Authentication required (use database users)")
                            ));
                        })
                );

        return http.build();
    }
}
