package com.example.securityplayground.scenario.filterchain;

import com.example.securityplayground.scenario.ScenarioDescriptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilterChainDescriptor implements ScenarioDescriptor {

    @Override
    public String getId() { return "filter-chain"; }

    @Override
    public String getName() { return "Filter Chain Visualization"; }

    @Override
    public String getDescription() {
        return "Visualize Spring Security's filter chain architecture. See all registered SecurityFilterChains, "
                + "their request matchers, and the ordered list of filters in each chain. "
                + "Understand how requests are routed through the security infrastructure.";
    }

    @Override
    public String getCategory() { return "Security Internals"; }

    @Override
    public String getPath() { return "/api/debug"; }

    @Override
    public List<EndpointInfo> getEndpoints() {
        return List.of(
                new EndpointInfo("GET", "/api/debug/filter-chains", "List all SecurityFilterChains with their filters", false),
                new EndpointInfo("GET", "/api/debug/filter-chain?path=/api/demo/jwt/protected", "Find matching filter chain for a given path", false),
                new EndpointInfo("GET", "/api/debug/security-context", "View the current SecurityContext", false)
        );
    }

    @Override
    public List<TestAccount> getTestAccounts() { return List.of(); }
}
