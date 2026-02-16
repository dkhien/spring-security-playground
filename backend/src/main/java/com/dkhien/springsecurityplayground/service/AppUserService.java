package com.dkhien.springsecurityplayground.service;

import com.dkhien.springsecurityplayground.entity.AppUser;
import com.dkhien.springsecurityplayground.exception.UserNotFoundException;
import com.dkhien.springsecurityplayground.exception.UsernameAlreadyTakenException;
import com.dkhien.springsecurityplayground.repository.AppUserRepository;
import com.dkhien.springsecurityplayground.security.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUser signup(String username, String password) {
        if (appUserRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyTakenException(username);
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        return appUserRepository.save(user);
    }

    public AppUser findByUsernameInternal(String username) {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public AppUser findByUsername(@Param("username") String username) {
        log.info("Name: " + SecurityContextHolder.getContext().getAuthentication().getName());
        return findByUsernameInternal(username);
    }

    public AppUser findByIdInternal(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));
    }

    @PreAuthorize("hasRole('ADMIN') or principal.id == #id")
    public AppUser findById(@Param("id") Long id) {
        return findByIdInternal(id);
    }

    public List<AppUser> findAll() {
        return appUserRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN') or principal.id == #id")
    public AppUser updateUser(@Param("id") Long id, String name, String email) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));
        if (name != null) {
            user.setName(name);
        }
        if (email != null) {
            user.setEmail(email);
        }
        return appUserRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AppUser updateRole(Long id, Role role) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));
        user.setRole(role);
        return appUserRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long id) {
        if (!appUserRepository.existsById(id)) {
            throw new UserNotFoundException(id.toString());
        }
        appUserRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public void changePassword(@Param("username") String username, String oldPassword, String newPassword) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        appUserRepository.save(user);
    }
}
