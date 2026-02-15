package com.dkhien.springsecurityplayground.controller.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jwt")
public class JwtUserController {

    @GetMapping("/me")
    public String me(Authentication authentication) {
        return "Hello, " + authentication.getName() + " (JWT)";
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint. (JWT)";
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "This is an admin endpoint. (JWT)";
    }
}
