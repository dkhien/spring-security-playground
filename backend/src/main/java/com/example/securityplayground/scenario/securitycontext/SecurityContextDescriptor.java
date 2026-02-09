package com.example.securityplayground.scenario.securitycontext;

import com.example.securityplayground.scenario.ScenarioDescriptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecurityContextDescriptor implements ScenarioDescriptor {

    @Override
    public String getId() { return "security-context"; }

    @Override
    public String getName() { return "SecurityContext Flow"; }

    @Override
    public String getDescription() {
        return "Explore how the SecurityContext and Authentication object flow through a request. "
                + "See how SecurityContextHolder stores the authentication, how it's populated by filters, "
                + "and how controllers access the authenticated principal.";
    }

    @Override
    public String getCategory() { return "Security Internals"; }

    @Override
    public String getPath() { return "/api/demo/security-context"; }

    @Override
    public List<EndpointInfo> getEndpoints() {
        return List.of(
                new EndpointInfo("GET", "/api/demo/security-context/inspect", "Inspect SecurityContext - works with or without auth", false),
                new EndpointInfo("GET", "/api/demo/security-context/thread-info", "Show thread-local SecurityContext details", false)
        );
    }

    @Override
    public List<TestAccount> getTestAccounts() {
        return List.of(
                new TestAccount("user", "password", "USER", "Try with and without credentials")
        );
    }
}
