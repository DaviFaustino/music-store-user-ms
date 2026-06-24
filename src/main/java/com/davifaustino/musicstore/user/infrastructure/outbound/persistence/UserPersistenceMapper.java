package com.davifaustino.musicstore.user.infrastructure.outbound.persistence;

import org.springframework.stereotype.Component;

import com.davifaustino.musicstore.user.domain.User;

@Component
public class UserPersistenceMapper {

    public UserEntity toEntity(User user) {
        var userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail());
        return userEntity;
    }

    public User toDomain(UserEntity userEntity) {
        var user = new User();
        user.setId(userEntity.getId());
        user.setName(userEntity.getName());
        user.setEmail(userEntity.getEmail());
        return user;
    }
}
