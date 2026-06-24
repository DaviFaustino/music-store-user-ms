package com.davifaustino.musicstore.user.application;

import org.springframework.stereotype.Component;

import com.davifaustino.musicstore.user.domain.User;
import com.davifaustino.musicstore.user.infrastructure.inbound.web.UserRequest;

@Component
public class UserMapper {

    public User toDomain(UserRequest userRequest) {
        var userModel = new User();
        userModel.setName(userRequest.name());
        userModel.setEmail(userRequest.email());
        return userModel;
    }
}
