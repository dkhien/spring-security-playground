package com.dkhien.springsecurityplayground.controller.basicauth;

import com.dkhien.springsecurityplayground.api.basicauth.BasicAuthApi;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicAuthController implements BasicAuthApi {

    @Override
    public ResponseEntity<String> basicAuthMe() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok("Hello, " + name + " (Basic Auth)");
    }

    @Override
    public ResponseEntity<String> basicAuthPublic() {
        return ResponseEntity.ok("This is a public endpoint. (Basic Auth)");
    }

    @Override
    public ResponseEntity<String> basicAuthAdmin() {
        return ResponseEntity.ok("This is an admin endpoint. (Basic Auth)");
    }
}
