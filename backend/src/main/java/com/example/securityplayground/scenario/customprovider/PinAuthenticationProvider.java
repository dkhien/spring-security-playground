package com.example.securityplayground.scenario.customprovider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Map;

public class PinAuthenticationProvider implements AuthenticationProvider {

    private static final Map<String, PinUser> PIN_USERS = Map.of(
            "alice", new PinUser("alice", "1234", List.of("ROLE_USER")),
            "bob", new PinUser("bob", "5678", List.of("ROLE_ADMIN", "ROLE_USER"))
    );

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pin = authentication.getCredentials().toString();

        PinUser user = PIN_USERS.get(username);
        if (user == null) {
            throw new BadCredentialsException("Unknown user: " + username);
        }

        if (!user.pin().equals(pin)) {
            throw new BadCredentialsException("Invalid PIN for user: " + username);
        }

        return new UsernamePasswordAuthenticationToken(
                username, null,
                user.roles().stream().map(SimpleGrantedAuthority::new).toList()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    record PinUser(String username, String pin, List<String> roles) {}
}
