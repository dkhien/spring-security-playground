package com.example.securityplayground.scenario;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ScenarioRegistry {

    private final List<ScenarioDescriptor> scenarios;

    public ScenarioRegistry(List<ScenarioDescriptor> scenarios) {
        this.scenarios = scenarios;
    }

    public List<ScenarioDescriptor> getAllScenarios() {
        return scenarios;
    }

    public Optional<ScenarioDescriptor> findById(String id) {
        return scenarios.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    public Map<String, List<ScenarioDescriptor>> getByCategory() {
        return scenarios.stream()
                .collect(Collectors.groupingBy(ScenarioDescriptor::getCategory));
    }
}
