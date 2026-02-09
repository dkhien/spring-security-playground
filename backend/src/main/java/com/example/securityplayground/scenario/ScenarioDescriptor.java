package com.example.securityplayground.scenario;

import java.util.List;

public interface ScenarioDescriptor {

    String getId();

    String getName();

    String getDescription();

    String getCategory();

    String getPath();

    List<EndpointInfo> getEndpoints();

    List<TestAccount> getTestAccounts();

    record EndpointInfo(String method, String path, String description, boolean requiresAuth) {}

    record TestAccount(String username, String password, String role, String notes) {}
}
