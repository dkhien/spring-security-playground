package com.dkhien.springsecurityplayground.controller.basicauth;

import com.dkhien.springsecurityplayground.api.basicauth.BasicAuthApi;
import com.dkhien.springsecurityplayground.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BasicAuthController implements BasicAuthApi {
    private final AppUserService appUserService;

    @Override
    public ResponseEntity<String> basicAuthMe() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        var appUser = appUserService.findByUsername(name);
        return ResponseEntity.ok("(Basic Auth) Hello, " + appUser.getName());
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
