package com.dkhien.springsecurityplayground.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {

    @GetMapping("/me")
    public String user(HttpSession session) {
        var securityContext = SecurityContextHolder.getContext();
        var authentication = securityContext.getAuthentication();
        var principal = authentication.getPrincipal();
        var name = "UNKNOWN";
        if (principal instanceof Principal) {
            name = ((Principal) principal).getName();
        } else if (principal instanceof User) {
            name = ((User) principal).getUsername();
        }
        return "Hello, " + name + "\nType: " + principal.getClass() + "\nSession ID: " + session.getId();
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint.";
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "This is an admin endpoint.";
    }
}
