package com.example.securityplayground.scenario.dbusers;

import com.example.securityplayground.scenario.ScenarioDescriptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DbUsersDescriptor implements ScenarioDescriptor {

    @Override
    public String getId() { return "db-users"; }

    @Override
    public String getName() { return "Database Users"; }

    @Override
    public String getDescription() {
        return "Users stored in a PostgreSQL database via JPA. Implements UserDetailsService to load users from "
                + "the app_user table. Demonstrates real-world user management with roles, enabled/disabled, and locked accounts.";
    }

    @Override
    public String getCategory() { return "Authentication Basics"; }

    @Override
    public String getPath() { return "/api/demo/db-users"; }

    @Override
    public List<EndpointInfo> getEndpoints() {
        return List.of(
                new EndpointInfo("GET", "/api/demo/db-users/protected", "Protected - requires DB user credentials", true),
                new EndpointInfo("GET", "/api/demo/db-users/me", "Get current DB user details with roles", true),
                new EndpointInfo("GET", "/api/demo/db-users/users", "List all users in the database", false)
        );
    }

    @Override
    public List<TestAccount> getTestAccounts() {
        return List.of(
                new TestAccount("user", "password", "USER", "Standard user"),
                new TestAccount("admin", "admin", "ADMIN,USER", "Administrator"),
                new TestAccount("manager", "manager", "MANAGER,USER", "Manager"),
                new TestAccount("locked", "locked", "USER", "Account is locked"),
                new TestAccount("disabled", "disabled", "USER", "Account is disabled")
        );
    }
}
