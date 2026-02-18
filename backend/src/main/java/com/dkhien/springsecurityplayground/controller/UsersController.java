package com.dkhien.springsecurityplayground.controller;

import com.dkhien.springsecurityplayground.api.users.UsersApi;
import com.dkhien.springsecurityplayground.mapper.AppUserMapper;
import com.dkhien.springsecurityplayground.model.users.ChangePasswordRequest;
import com.dkhien.springsecurityplayground.model.users.UpdateRoleRequest;
import com.dkhien.springsecurityplayground.model.users.UpdateUserRequest;
import com.dkhien.springsecurityplayground.model.users.UserResponse;
import com.dkhien.springsecurityplayground.entity.Role;
import com.dkhien.springsecurityplayground.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UsersController implements UsersApi {

    private final AppUserService appUserService;

    private final AppUserMapper appUserMapper;

    @Override
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        var allAppUsers = appUserService.findAll();
        var userResponseList = allAppUsers.stream().map(appUserMapper::toUserResponse).toList();
        return ResponseEntity.ok(userResponseList);
    }

    @Override
    public ResponseEntity<UserResponse> getUserById(Long id) {
        var appUser = appUserService.findById(id);
        return ResponseEntity.ok(appUserMapper.toUserResponse(appUser));
    }

    @Override
    public ResponseEntity<UserResponse> getCurrentUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        var appUser = appUserService.findByUsername(name);
        return ResponseEntity.ok(appUserMapper.toUserResponse(appUser));
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(Long id, UpdateUserRequest updateUserRequest) {
        var appUser = appUserService.updateUser(id, updateUserRequest.getName(), updateUserRequest.getEmail());
        return ResponseEntity.ok(appUserMapper.toUserResponse(appUser));
    }

    @Override
    public ResponseEntity<UserResponse> updateUserRole(Long id, UpdateRoleRequest updateRoleRequest) {
        var appUser = appUserService.updateRole(id, Role.valueOf(updateRoleRequest.getRole().getValue()));
        return ResponseEntity.ok(appUserMapper.toUserResponse(appUser));
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        appUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> changePassword(ChangePasswordRequest changePasswordRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        appUserService.changePassword(username, changePasswordRequest.getOldPassword(),
                changePasswordRequest.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
