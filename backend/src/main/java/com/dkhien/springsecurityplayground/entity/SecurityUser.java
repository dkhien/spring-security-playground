package com.dkhien.springsecurityplayground.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

@Getter
public class SecurityUser extends User {

    private final Long id;

    public SecurityUser(Long id, String username, String password,
                        Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public static SecurityUser from(AppUser appUser) {
        return new SecurityUser(
                appUser.getId(),
                appUser.getUsername(),
                appUser.getPassword(),
                List.of(new SimpleGrantedAuthority(appUser.getRole().authority()))
        );
    }
}
