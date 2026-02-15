package com.dkhien.springsecurityplayground.controller.session;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @GetMapping("/me")
    public String me(Authentication authentication, HttpSession session) {
        return "Hello, " + authentication.getName() + " (Session)\nSession ID: " + session.getId();
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint. (Session)";
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "This is an admin endpoint. (Session)";
    }
}
