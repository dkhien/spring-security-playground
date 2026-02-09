package com.example.securityplayground.scenario.owasp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ContentSecurityPolicyHeaderWriter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class OwaspSecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    public OwaspSecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    @Order(30)
    public SecurityFilterChain owaspFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/demo/owasp/**")
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .headers(headers -> headers
                        .contentTypeOptions(cto -> {})
                        .frameOptions(fo -> fo.deny())
                        .httpStrictTransportSecurity(hsts -> hsts
                                .maxAgeInSeconds(31536000)
                                .includeSubDomains(true)
                        )
                        .referrerPolicy(rp -> rp.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                        .addHeaderWriter(new ContentSecurityPolicyHeaderWriter("default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline'"))
                );

        return http.build();
    }
}
