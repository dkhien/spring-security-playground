package com.example.securityplayground.scenario.httpbasic;

import com.example.securityplayground.scenario.ScenarioDescriptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HttpBasicDescriptor implements ScenarioDescriptor {

    @Override
    public String getId() { return "http-basic"; }

    @Override
    public String getName() { return "HTTP Basic Auth"; }

    @Override
    public String getDescription() {
        return "HTTP Basic authentication sends credentials in the Authorization header (Base64-encoded). "
                + "Simple but sends credentials with every request. Best suited for API-to-API communication or testing.";
    }

    @Override
    public String getCategory() { return "Authentication Basics"; }

    @Override
    public String getPath() { return "/api/demo/http-basic"; }

    @Override
    public List<EndpointInfo> getEndpoints() {
        return List.of(
                new EndpointInfo("GET", "/api/demo/http-basic/protected", "Protected resource - requires Basic auth", true),
                new EndpointInfo("GET", "/api/demo/http-basic/me", "Get current user details", true),
                new EndpointInfo("GET", "/api/demo/http-basic/public", "Public endpoint - no auth required", false)
        );
    }

    @Override
    public List<TestAccount> getTestAccounts() {
        return List.of(
                new TestAccount("user", "password", "USER", "Standard user"),
                new TestAccount("admin", "admin", "ADMIN", "Administrator")
        );
    }
}
