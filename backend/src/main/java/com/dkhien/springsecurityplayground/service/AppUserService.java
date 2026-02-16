package com.dkhien.springsecurityplayground.service;

import com.dkhien.springsecurityplayground.entity.AppUser;
import com.dkhien.springsecurityplayground.repository.AppUserRepository;
import com.dkhien.springsecurityplayground.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUser signup(String username, String password) {
        if (appUserRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already taken");
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        return appUserRepository.save(user);
    }
}
