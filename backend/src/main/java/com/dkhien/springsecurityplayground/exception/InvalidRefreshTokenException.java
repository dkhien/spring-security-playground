package com.dkhien.springsecurityplayground.exception;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException(String reason) {
        super("Invalid refresh token: " + reason);
    }
}
