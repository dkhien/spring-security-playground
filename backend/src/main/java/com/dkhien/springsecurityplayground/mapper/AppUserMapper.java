package com.dkhien.springsecurityplayground.mapper;

import com.dkhien.springsecurityplayground.entity.AppUser;
import com.dkhien.springsecurityplayground.model.users.UserResponse;
import org.mapstruct.Mapper;

@Mapper
public interface AppUserMapper {
    UserResponse toUserResponse(AppUser appUser);
}
