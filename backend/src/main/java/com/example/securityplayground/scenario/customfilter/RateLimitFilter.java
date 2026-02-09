package com.example.securityplayground.scenario.customfilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS = 5;
    private static final long WINDOW_MS = 30_000;

    private final ConcurrentHashMap<String, CopyOnWriteArrayList<Long>> requestCounts = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public RateLimitFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientIp = request.getRemoteAddr();
        long now = System.currentTimeMillis();

        CopyOnWriteArrayList<Long> timestamps = requestCounts.computeIfAbsent(clientIp, k -> new CopyOnWriteArrayList<>());

        // Remove expired timestamps
        timestamps.removeIf(t -> now - t > WINDOW_MS);
        timestamps.add(now);

        response.setHeader("X-RateLimit-Limit", String.valueOf(MAX_REQUESTS));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(Math.max(0, MAX_REQUESTS - timestamps.size())));

        if (timestamps.size() > MAX_REQUESTS) {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(
                    Map.of("success", false,
                            "message", "Rate limit exceeded. Max " + MAX_REQUESTS + " requests per " + (WINDOW_MS / 1000) + " seconds.",
                            "data", Map.of(
                                    "limit", MAX_REQUESTS,
                                    "windowSeconds", WINDOW_MS / 1000,
                                    "currentCount", timestamps.size(),
                                    "retryAfterMs", WINDOW_MS - (now - timestamps.get(0))
                            ))
            ));
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().contains("/rate-limited");
    }
}
