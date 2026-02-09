package com.example.securityplayground.scenario.rolebased;

import com.example.securityplayground.scenario.ScenarioDescriptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleBasedDescriptor implements ScenarioDescriptor {

    @Override
    public String getId() { return "role-based"; }

    @Override
    public String getName() { return "Role-Based Access"; }

    @Override
    public String getDescription() {
        return "URL-level authorization using roles. Different endpoints require different roles (USER, ADMIN, MANAGER). "
                + "Demonstrates hasRole(), hasAnyRole(), and role hierarchy concepts.";
    }

    @Override
    public String getCategory() { return "Authorization"; }

    @Override
    public String getPath() { return "/api/demo/role-based"; }

    @Override
    public List<EndpointInfo> getEndpoints() {
        return List.of(
                new EndpointInfo("GET", "/api/demo/role-based/public", "No auth required", false),
                new EndpointInfo("GET", "/api/demo/role-based/user", "Requires ROLE_USER", true),
                new EndpointInfo("GET", "/api/demo/role-based/manager", "Requires ROLE_MANAGER", true),
                new EndpointInfo("GET", "/api/demo/role-based/admin", "Requires ROLE_ADMIN", true)
        );
    }

    @Override
    public List<TestAccount> getTestAccounts() {
        return List.of(
                new TestAccount("user", "password", "USER", "Can access /user"),
                new TestAccount("manager", "manager", "MANAGER,USER", "Can access /user and /manager"),
                new TestAccount("admin", "admin", "ADMIN,USER", "Can access /user and /admin")
        );
    }
}
