package com.example.securityplayground.scenario.owasp;

import com.example.securityplayground.scenario.ScenarioDescriptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OwaspDescriptor implements ScenarioDescriptor {

    @Override
    public String getId() { return "owasp"; }

    @Override
    public String getName() { return "OWASP Security Headers"; }

    @Override
    public String getDescription() {
        return "Explore OWASP security best practices: CSRF protection, CORS configuration, "
                + "security headers (X-Content-Type-Options, X-Frame-Options, Content-Security-Policy), "
                + "and XSS prevention. Compare responses with and without security headers.";
    }

    @Override
    public String getCategory() { return "OWASP"; }

    @Override
    public String getPath() { return "/api/demo/owasp"; }

    @Override
    public List<EndpointInfo> getEndpoints() {
        return List.of(
                new EndpointInfo("GET", "/api/demo/owasp/headers", "Check security headers on this response", false),
                new EndpointInfo("GET", "/api/demo/owasp/cors-info", "Show current CORS configuration", false),
                new EndpointInfo("GET", "/api/demo/owasp/csrf-info", "Show CSRF protection status", false)
        );
    }

    @Override
    public List<TestAccount> getTestAccounts() { return List.of(); }
}
