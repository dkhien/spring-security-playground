package com.example.securityplayground.scenario.customfilter;

import com.example.securityplayground.scenario.ScenarioDescriptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomFilterDescriptor implements ScenarioDescriptor {

    @Override
    public String getId() { return "custom-filter"; }

    @Override
    public String getName() { return "Custom Filters"; }

    @Override
    public String getDescription() {
        return "Build custom security filters: an API Key authentication filter and a rate-limiting filter. "
                + "Learn how to add custom filters at specific positions in the filter chain.";
    }

    @Override
    public String getCategory() { return "Customization"; }

    @Override
    public String getPath() { return "/api/demo/custom-filter"; }

    @Override
    public List<EndpointInfo> getEndpoints() {
        return List.of(
                new EndpointInfo("GET", "/api/demo/custom-filter/api-key", "Protected by API Key filter (header: X-API-Key)", true),
                new EndpointInfo("GET", "/api/demo/custom-filter/rate-limited", "Rate-limited endpoint (max 5 requests per 30 seconds)", false),
                new EndpointInfo("GET", "/api/demo/custom-filter/public", "Public endpoint - no custom filter", false)
        );
    }

    @Override
    public List<TestAccount> getTestAccounts() {
        return List.of(
                new TestAccount("N/A", "demo-api-key-12345", "API_KEY", "Use as X-API-Key header value"),
                new TestAccount("N/A", "admin-api-key-99999", "API_KEY", "Admin API key")
        );
    }
}
