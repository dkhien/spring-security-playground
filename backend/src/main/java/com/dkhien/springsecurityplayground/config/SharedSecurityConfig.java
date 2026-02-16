package com.dkhien.springsecurityplayground.config;

import com.dkhien.springsecurityplayground.security.Role;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;

@Configuration
public class SharedSecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Profile("inmemory")
    UserDetailsService inMemoryUserDetailsService(PasswordEncoder encoder) {
        UserDetails normalUser = User.withUsername("user")
                .password(encoder.encode("password"))
                .roles(Role.USER.name())
                .build();
        UserDetails adminUser = User.withUsername("admin")
                .password(encoder.encode("password"))
                .roles(Role.ADMIN.name())
                .build();
        return new InMemoryUserDetailsManager(List.of(normalUser, adminUser));
    }

    @Bean
    @Profile("db")
    UserDetailsService dbUserDetailsService(
            @Qualifier("customUserDetailsService") UserDetailsService customUserDetailsService) {
        return customUserDetailsService;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
