package com.dkhien.springsecurityplayground.service;

import com.dkhien.springsecurityplayground.exception.UserNotFoundException;
import com.dkhien.springsecurityplayground.entity.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service("customUserDetailsService")
@Profile("db")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserService appUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return SecurityUser.from(appUserService.findByUsernameInternal(username));
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException("User " + username + " not found in database");
        }
    }
}
