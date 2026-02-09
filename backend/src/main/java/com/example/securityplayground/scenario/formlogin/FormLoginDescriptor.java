package com.example.securityplayground.scenario.formlogin;

import com.example.securityplayground.scenario.ScenarioDescriptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FormLoginDescriptor implements ScenarioDescriptor {

    @Override
    public String getId() { return "form-login"; }

    @Override
    public String getName() { return "Form Login"; }

    @Override
    public String getDescription() {
        return "Traditional form-based authentication using Spring Security's built-in form login support. "
                + "Demonstrates login/logout endpoints, session creation, and authentication success/failure handling.";
    }

    @Override
    public String getCategory() { return "Authentication Basics"; }

    @Override
    public String getPath() { return "/api/demo/form-login"; }

    @Override
    public List<EndpointInfo> getEndpoints() {
        return List.of(
                new EndpointInfo("POST", "/api/demo/form-login/login", "Authenticate with username/password (form data)", false),
                new EndpointInfo("GET", "/api/demo/form-login/me", "Get current authenticated user info", true),
                new EndpointInfo("POST", "/api/demo/form-login/logout", "Logout and invalidate session", true),
                new EndpointInfo("GET", "/api/demo/form-login/public", "Public endpoint - no auth required", false)
        );
    }

    @Override
    public List<TestAccount> getTestAccounts() {
        return List.of(
                new TestAccount("user", "password", "USER", "Standard user"),
                new TestAccount("admin", "admin", "ADMIN", "Administrator"),
                new TestAccount("locked", "locked", "USER", "Account is locked"),
                new TestAccount("disabled", "disabled", "USER", "Account is disabled")
        );
    }
}
