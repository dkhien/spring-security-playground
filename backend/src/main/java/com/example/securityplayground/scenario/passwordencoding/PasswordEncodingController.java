package com.example.securityplayground.scenario.passwordencoding;

import com.example.securityplayground.common.dto.ApiResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/demo/password-encoding")
public class PasswordEncodingController {

    private final Map<String, PasswordEncoder> encoders = Map.of(
            "bcrypt", new BCryptPasswordEncoder(),
            "bcrypt-4", new BCryptPasswordEncoder(4),
            "bcrypt-12", new BCryptPasswordEncoder(12),
            "scrypt", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8()
    );

    @PostMapping("/encode")
    public ApiResponse<Map<String, Object>> encode(@RequestBody Map<String, String> request) {
        String rawPassword = request.getOrDefault("password", "secret");
        String algorithm = request.getOrDefault("algorithm", "bcrypt");

        PasswordEncoder encoder = encoders.getOrDefault(algorithm, encoders.get("bcrypt"));

        long start = System.nanoTime();
        String encoded = encoder.encode(rawPassword);
        long durationMs = (System.nanoTime() - start) / 1_000_000;

        return ApiResponse.ok(Map.of(
                "algorithm", algorithm,
                "rawPassword", rawPassword,
                "encodedPassword", encoded,
                "encodingTimeMs", durationMs,
                "hashLength", encoded.length()
        ));
    }

    @PostMapping("/verify")
    public ApiResponse<Map<String, Object>> verify(@RequestBody Map<String, String> request) {
        String rawPassword = request.getOrDefault("password", "");
        String encodedPassword = request.getOrDefault("hash", "");
        String algorithm = request.getOrDefault("algorithm", "bcrypt");

        PasswordEncoder encoder = encoders.getOrDefault(algorithm, encoders.get("bcrypt"));

        long start = System.nanoTime();
        boolean matches = encoder.matches(rawPassword, encodedPassword);
        long durationMs = (System.nanoTime() - start) / 1_000_000;

        return ApiResponse.ok(Map.of(
                "matches", matches,
                "verificationTimeMs", durationMs
        ));
    }

    @PostMapping("/compare")
    public ApiResponse<Map<String, Object>> compare(@RequestBody Map<String, String> request) {
        String rawPassword = request.getOrDefault("password", "secret");

        Map<String, Object> results = new LinkedHashMap<>();
        for (Map.Entry<String, PasswordEncoder> entry : encoders.entrySet()) {
            long start = System.nanoTime();
            String encoded = entry.getValue().encode(rawPassword);
            long durationMs = (System.nanoTime() - start) / 1_000_000;

            results.put(entry.getKey(), Map.of(
                    "encoded", encoded,
                    "timeMs", durationMs,
                    "hashLength", encoded.length()
            ));
        }

        return ApiResponse.ok(Map.of(
                "password", rawPassword,
                "results", results,
                "availableAlgorithms", encoders.keySet()
        ));
    }
}
