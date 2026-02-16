package com.dkhien.springsecurityplayground.controller;

import com.dkhien.springsecurityplayground.api.auth.AuthApi;
import com.dkhien.springsecurityplayground.entity.AppUser;
import com.dkhien.springsecurityplayground.model.auth.SignupRequest;
import com.dkhien.springsecurityplayground.model.auth.SignupResponse;
import com.dkhien.springsecurityplayground.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AppUserService appUserService;

    @Override
    public ResponseEntity<SignupResponse> signup(SignupRequest signupRequest) {
        AppUser user = appUserService.signup(signupRequest.getUsername(), signupRequest.getPassword());
        SignupResponse response = new SignupResponse()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
