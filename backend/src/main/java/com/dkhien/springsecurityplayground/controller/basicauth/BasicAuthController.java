package com.dkhien.springsecurityplayground.controller.basicauth;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/basic-auth")
public class BasicAuthController {

    @GetMapping("/me")
    public String me(Authentication authentication) {
        return "Hello, " + authentication.getName() + " (Basic Auth)";
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint. (Basic Auth)";
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "This is an admin endpoint. (Basic Auth)";
    }
}
