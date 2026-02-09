package com.example.securityplayground.scenario.jwt;

import com.example.securityplayground.scenario.ScenarioDescriptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtDescriptor implements ScenarioDescriptor {

    @Override
    public String getId() { return "jwt"; }

    @Override
    public String getName() { return "JWT Authentication"; }

    @Override
    public String getDescription() {
        return "Stateless authentication using JSON Web Tokens (JWT). Login to receive an access token and refresh token. "
                + "Access token is short-lived; use refresh token to get a new access token. Includes token decode visualization.";
    }

    @Override
    public String getCategory() { return "JWT / Stateless"; }

    @Override
    public String getPath() { return "/api/demo/jwt"; }

    @Override
    public List<EndpointInfo> getEndpoints() {
        return List.of(
                new EndpointInfo("POST", "/api/demo/jwt/login", "Login and receive JWT tokens", false),
                new EndpointInfo("POST", "/api/demo/jwt/refresh", "Refresh access token using refresh token", false),
                new EndpointInfo("GET", "/api/demo/jwt/protected", "Protected resource - requires valid JWT", true),
                new EndpointInfo("GET", "/api/demo/jwt/me", "Get current user info from JWT claims", true),
                new EndpointInfo("POST", "/api/demo/jwt/decode", "Decode a JWT token (no verification)", false)
        );
    }

    @Override
    public List<TestAccount> getTestAccounts() {
        return List.of(
                new TestAccount("user", "password", "USER", "Standard user"),
                new TestAccount("admin", "admin", "ADMIN,USER", "Administrator")
        );
    }
}
