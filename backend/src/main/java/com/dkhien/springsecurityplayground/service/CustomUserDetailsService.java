package com.dkhien.springsecurityplayground.service;

import com.dkhien.springsecurityplayground.entity.AppUser;
import com.dkhien.springsecurityplayground.exception.UserNotFoundException;
import com.dkhien.springsecurityplayground.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("customUserDetailsService")
@Profile("db")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserService appUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            AppUser appUser = appUserService.findByUsername(username);
            return new SecurityUser(
                    appUser.getId(),
                    appUser.getUsername(),
                    appUser.getPassword(),
                    List.of(new SimpleGrantedAuthority(appUser.getRole().authority()))
            );
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException("User " + username + " not found in database");
        }
    }
}
