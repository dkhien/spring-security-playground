package com.dkhien.springsecurityplayground.exception;

public class InvalidOtpException extends RuntimeException {
    public InvalidOtpException() {
        super("Invalid OTP code");
    }
}
