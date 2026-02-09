package com.example.securityplayground.scenario.inmemory;

import com.example.securityplayground.scenario.ScenarioDescriptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InMemoryDescriptor implements ScenarioDescriptor {

    @Override
    public String getId() { return "in-memory"; }

    @Override
    public String getName() { return "In-Memory Users"; }

    @Override
    public String getDescription() {
        return "Users defined directly in Java config using InMemoryUserDetailsManager. "
                + "Great for prototyping and testing - no database needed. "
                + "Note: these users are separate from the DB users.";
    }

    @Override
    public String getCategory() { return "Authentication Basics"; }

    @Override
    public String getPath() { return "/api/demo/in-memory"; }

    @Override
    public List<EndpointInfo> getEndpoints() {
        return List.of(
                new EndpointInfo("GET", "/api/demo/in-memory/protected", "Protected - requires in-memory user credentials", true),
                new EndpointInfo("GET", "/api/demo/in-memory/users", "List all configured in-memory users", false)
        );
    }

    @Override
    public List<TestAccount> getTestAccounts() {
        return List.of(
                new TestAccount("alice", "alice123", "USER", "In-memory user"),
                new TestAccount("bob", "bob123", "ADMIN", "In-memory admin")
        );
    }
}
