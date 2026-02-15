package com.dkhien.springsecurityplayground.dto;

public record LoginResponse(String accessToken, String refreshToken) {
}
