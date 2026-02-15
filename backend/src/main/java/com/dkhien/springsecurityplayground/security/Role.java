package com.dkhien.springsecurityplayground.security;

public enum Role {
    USER,
    ADMIN;

    private static final String PREFIX = "ROLE_";

    /**
     * Returns the authority string with ROLE_ prefix (e.g. "ROLE_ADMIN").
     * Use with .authorities() and .hasAuthority().
     */
    public String authority() {
        return PREFIX + name();
    }
}
