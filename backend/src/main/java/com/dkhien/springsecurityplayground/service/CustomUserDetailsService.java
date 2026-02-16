package com.dkhien.springsecurityplayground.service;

import com.dkhien.springsecurityplayground.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userOptional = appUserRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            var appUser = userOptional.get();
            return User.withUsername(appUser.getUsername())
                    .password(appUser.getPassword())
                    .roles(appUser.getRole().name())
                    .build();
        } else {
            throw new UsernameNotFoundException("User " + username + " not found in database");
        }
    }
}
