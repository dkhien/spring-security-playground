package com.example.securityplayground.scenario.customfilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Map;

@Configuration
public class CustomFilterSecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final ObjectMapper objectMapper;

    public CustomFilterSecurityConfig(CorsConfigurationSource corsConfigurationSource, ObjectMapper objectMapper) {
        this.corsConfigurationSource = corsConfigurationSource;
        this.objectMapper = objectMapper;
    }

    @Bean
    @Order(25)
    public SecurityFilterChain customFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/demo/custom-filter/**")
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new ApiKeyAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new RateLimitFilter(objectMapper), ApiKeyAuthFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/demo/custom-filter/public", "/api/demo/custom-filter/rate-limited").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write(objectMapper.writeValueAsString(
                                    Map.of("success", false, "message", "API Key required. Send X-API-Key header.")
                            ));
                        })
                );

        return http.build();
    }
}
