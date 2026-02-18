package com.dkhien.springsecurityplayground.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "totp")
public record TotpConfig (String issuer, int digits, int period) {
}
