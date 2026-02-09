package com.example.securityplayground.scenario.passwordencoding;

import com.example.securityplayground.scenario.ScenarioDescriptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PasswordEncodingDescriptor implements ScenarioDescriptor {

    @Override
    public String getId() { return "password-encoding"; }

    @Override
    public String getName() { return "Password Encoding"; }

    @Override
    public String getDescription() {
        return "Compare password encoding algorithms: BCrypt, SCrypt, PBKDF2, Argon2, and plain text. "
                + "See how each algorithm hashes passwords and the time it takes.";
    }

    @Override
    public String getCategory() { return "Authentication Basics"; }

    @Override
    public String getPath() { return "/api/demo/password-encoding"; }

    @Override
    public List<EndpointInfo> getEndpoints() {
        return List.of(
                new EndpointInfo("POST", "/api/demo/password-encoding/encode", "Encode a password with different algorithms", false),
                new EndpointInfo("POST", "/api/demo/password-encoding/verify", "Verify a password against a hash", false),
                new EndpointInfo("POST", "/api/demo/password-encoding/compare", "Compare encoding times across algorithms", false)
        );
    }

    @Override
    public List<TestAccount> getTestAccounts() { return List.of(); }
}
