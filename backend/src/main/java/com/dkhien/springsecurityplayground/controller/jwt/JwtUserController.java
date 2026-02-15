package com.dkhien.springsecurityplayground.controller.jwt;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/jwt")
public class JwtUserController {

    @GetMapping("/user")
    public String user() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var principal = authentication.getPrincipal();
        String name = "UNKNOWN";
        if (principal instanceof Principal p) {
            name = p.getName();
        } else if (principal instanceof User u) {
            name = u.getUsername();
        }
        return "Hello, " + name + " (JWT)";
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
