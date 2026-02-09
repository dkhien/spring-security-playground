package com.example.securityplayground.scenario.customprovider;

import com.example.securityplayground.scenario.ScenarioDescriptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomProviderDescriptor implements ScenarioDescriptor {

    @Override
    public String getId() { return "custom-provider"; }

    @Override
    public String getName() { return "Custom Auth Provider"; }

    @Override
    public String getDescription() {
        return "Implement a custom AuthenticationProvider that validates credentials using custom logic. "
                + "This demo uses a simple PIN-based authentication to demonstrate how to plug in your own provider.";
    }

    @Override
    public String getCategory() { return "Customization"; }

    @Override
    public String getPath() { return "/api/demo/custom-provider"; }

    @Override
    public List<EndpointInfo> getEndpoints() {
        return List.of(
                new EndpointInfo("POST", "/api/demo/custom-provider/login", "Login with username + PIN", false),
                new EndpointInfo("GET", "/api/demo/custom-provider/protected", "Protected resource", true)
        );
    }

    @Override
    public List<TestAccount> getTestAccounts() {
        return List.of(
                new TestAccount("alice", "1234", "USER", "PIN: 1234"),
                new TestAccount("bob", "5678", "ADMIN", "PIN: 5678")
        );
    }
}
