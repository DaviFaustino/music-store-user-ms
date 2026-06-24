package com.davifaustino.musicstore.user.infrastructure.outbound.persistence;

import org.springframework.stereotype.Component;

import com.davifaustino.musicstore.user.domain.User;

@Component
public class UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    public UserRepository(JpaUserRepository jpaUserRepository, UserPersistenceMapper userPersistenceMapper) {
        this.jpaUserRepository = jpaUserRepository;
        this.userPersistenceMapper = userPersistenceMapper;
    }

    public User save(User user) {
        var userEntity = userPersistenceMapper.toEntity(user);
        var savedUserEntity = jpaUserRepository.save(userEntity);
        return userPersistenceMapper.toDomain(savedUserEntity);
    }
}
