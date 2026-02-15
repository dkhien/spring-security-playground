package com.dkhien.springsecurityplayground.controller.jwt;

import com.dkhien.springsecurityplayground.api.jwt.JwtUserApi;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtUserController implements JwtUserApi {

    @Override
    public ResponseEntity<String> jwtMe() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok("Hello, " + name + " (JWT)");
    }

    @Override
    public ResponseEntity<String> jwtPublic() {
        return ResponseEntity.ok("This is a public endpoint. (JWT)");
    }

    @Override
    public ResponseEntity<String> jwtAdmin() {
        return ResponseEntity.ok("This is an admin endpoint. (JWT)");
    }
}
