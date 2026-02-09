package com.example.securityplayground.scenario.methodsecurity;

import com.example.securityplayground.scenario.ScenarioDescriptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MethodSecurityDescriptor implements ScenarioDescriptor {

    @Override
    public String getId() { return "method-security"; }

    @Override
    public String getName() { return "Method Security"; }

    @Override
    public String getDescription() {
        return "Method-level security using @PreAuthorize, @PostAuthorize, and @Secured annotations. "
                + "Authorization is enforced at the service/controller method level rather than URL patterns.";
    }

    @Override
    public String getCategory() { return "Authorization"; }

    @Override
    public String getPath() { return "/api/demo/method-security"; }

    @Override
    public List<EndpointInfo> getEndpoints() {
        return List.of(
                new EndpointInfo("GET", "/api/demo/method-security/pre-authorize-user", "@PreAuthorize(\"hasRole('USER')\")", true),
                new EndpointInfo("GET", "/api/demo/method-security/pre-authorize-admin", "@PreAuthorize(\"hasRole('ADMIN')\")", true),
                new EndpointInfo("GET", "/api/demo/method-security/secured-manager", "@Secured(\"ROLE_MANAGER\")", true),
                new EndpointInfo("GET", "/api/demo/method-security/pre-authorize-spel", "@PreAuthorize with SpEL expression", true)
        );
    }

    @Override
    public List<TestAccount> getTestAccounts() {
        return List.of(
                new TestAccount("user", "password", "USER", "Standard user"),
                new TestAccount("manager", "manager", "MANAGER,USER", "Manager"),
                new TestAccount("admin", "admin", "ADMIN,USER", "Administrator")
        );
    }
}
