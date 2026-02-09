package com.example.securityplayground.scenario.session;

import com.example.securityplayground.scenario.ScenarioDescriptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SessionDescriptor implements ScenarioDescriptor {

    @Override
    public String getId() { return "session"; }

    @Override
    public String getName() { return "Session Management"; }

    @Override
    public String getDescription() {
        return "Explore HTTP session behavior: session creation policies (ALWAYS, IF_REQUIRED, NEVER, STATELESS), "
                + "session fixation protection, and concurrent session control.";
    }

    @Override
    public String getCategory() { return "Authentication Basics"; }

    @Override
    public String getPath() { return "/api/demo/session"; }

    @Override
    public List<EndpointInfo> getEndpoints() {
        return List.of(
                new EndpointInfo("POST", "/api/demo/session/login", "Login to create a session", false),
                new EndpointInfo("GET", "/api/demo/session/info", "Get current session information", true),
                new EndpointInfo("POST", "/api/demo/session/logout", "Logout and invalidate session", true),
                new EndpointInfo("GET", "/api/demo/session/public", "Public endpoint - check session behavior", false)
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
