package com.example.securityplayground.scenario;

import com.example.securityplayground.common.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scenarios")
public class ScenarioController {

    private final ScenarioRegistry registry;

    public ScenarioController(ScenarioRegistry registry) {
        this.registry = registry;
    }

    @GetMapping
    public ApiResponse<List<ScenarioSummary>> listScenarios() {
        List<ScenarioSummary> summaries = registry.getAllScenarios().stream()
                .map(ScenarioSummary::from)
                .toList();
        return ApiResponse.ok(summaries);
    }

    @GetMapping("/grouped")
    public ApiResponse<Map<String, List<ScenarioSummary>>> listGrouped() {
        Map<String, List<ScenarioSummary>> grouped = registry.getByCategory().entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().map(ScenarioSummary::from).toList()
                ));
        return ApiResponse.ok(grouped);
    }

    @GetMapping("/{id}")
    public ApiResponse<ScenarioDetail> getScenario(@PathVariable String id) {
        return registry.findById(id)
                .map(s -> ApiResponse.ok(ScenarioDetail.from(s)))
                .orElse(ApiResponse.error("Scenario not found: " + id));
    }

    record ScenarioSummary(String id, String name, String description, String category, String path) {
        static ScenarioSummary from(ScenarioDescriptor s) {
            return new ScenarioSummary(s.getId(), s.getName(), s.getDescription(), s.getCategory(), s.getPath());
        }
    }

    record ScenarioDetail(
            String id, String name, String description, String category, String path,
            List<ScenarioDescriptor.EndpointInfo> endpoints,
            List<ScenarioDescriptor.TestAccount> testAccounts
    ) {
        static ScenarioDetail from(ScenarioDescriptor s) {
            return new ScenarioDetail(
                    s.getId(), s.getName(), s.getDescription(), s.getCategory(), s.getPath(),
                    s.getEndpoints(), s.getTestAccounts()
            );
        }
    }
}
