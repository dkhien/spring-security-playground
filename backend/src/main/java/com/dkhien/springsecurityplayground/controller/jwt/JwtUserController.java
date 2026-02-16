package com.dkhien.springsecurityplayground.controller.jwt;

import com.dkhien.springsecurityplayground.api.jwt.JwtUserApi;
import com.dkhien.springsecurityplayground.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtUserController implements JwtUserApi {
    private final AppUserService appUserService;

    @Override
    public ResponseEntity<String> jwtMe() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        var appUser = appUserService.findByUsername(name);
        return ResponseEntity.ok("(JWT) Hello, " + appUser.getName());
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
