package com.example.securityplayground.debug;

import jakarta.servlet.Filter;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilterChainDiagnosticService {

    private final FilterChainProxy filterChainProxy;

    public FilterChainDiagnosticService(FilterChainProxy filterChainProxy) {
        this.filterChainProxy = filterChainProxy;
    }

    public List<Map<String, Object>> getAllFilterChains() {
        List<SecurityFilterChain> chains = filterChainProxy.getFilterChains();
        List<Map<String, Object>> result = new ArrayList<>();

        for (int i = 0; i < chains.size(); i++) {
            SecurityFilterChain chain = chains.get(i);
            Map<String, Object> chainInfo = new LinkedHashMap<>();
            chainInfo.put("index", i);
            chainInfo.put("chainClass", chain.getClass().getSimpleName());

            if (chain instanceof DefaultSecurityFilterChain defaultChain) {
                chainInfo.put("requestMatcher", defaultChain.getRequestMatcher().toString());
            }

            chainInfo.put("filters", chain.getFilters().stream()
                    .map(f -> Map.of(
                            "name", f.getClass().getSimpleName(),
                            "fullClass", f.getClass().getName()
                    ))
                    .collect(Collectors.toList()));

            chainInfo.put("filterCount", chain.getFilters().size());
            result.add(chainInfo);
        }

        return result;
    }

    public Map<String, Object> getFilterChainForPath(String path) {
        List<SecurityFilterChain> chains = filterChainProxy.getFilterChains();

        for (int i = 0; i < chains.size(); i++) {
            SecurityFilterChain chain = chains.get(i);
            // We can't easily test matching without a real request, so provide all chains
            // and indicate which might match based on the matcher pattern
            if (chain instanceof DefaultSecurityFilterChain defaultChain) {
                String matcher = defaultChain.getRequestMatcher().toString();
                if (path != null && matchesPattern(matcher, path)) {
                    Map<String, Object> result = new LinkedHashMap<>();
                    result.put("matchedChainIndex", i);
                    result.put("requestMatcher", matcher);
                    result.put("filters", chain.getFilters().stream()
                            .map(Filter::getClass)
                            .map(Class::getSimpleName)
                            .collect(Collectors.toList()));
                    return result;
                }
            }
        }

        return Map.of("message", "No matching filter chain found for path: " + path);
    }

    private boolean matchesPattern(String matcher, String path) {
        // Simple pattern matching for demo purposes
        return matcher.contains(path) || path.startsWith(matcher.replace("/**", "").replace("*", ""));
    }
}
