package com.davifaustino.musicstore.user.application;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.davifaustino.musicstore.user.application.outbox.OutboxEvent;
import com.davifaustino.musicstore.user.domain.UserCreatedEvent;
import com.davifaustino.musicstore.user.infrastructure.inbound.web.UserRequest;
import com.davifaustino.musicstore.user.infrastructure.outbound.persistence.UserRepository;
import com.davifaustino.musicstore.user.infrastructure.outbound.persistence.outbox.OutboxRepository;

import jakarta.transaction.Transactional;
import tools.jackson.databind.ObjectMapper;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final OutboxRepository outboxRepository;

    public UserService(UserMapper userMapper, ObjectMapper objectMapper, UserRepository userRepository, OutboxRepository outboxRepository) {
        this.userMapper = userMapper;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.outboxRepository = outboxRepository;
    }

    @Transactional
    public UUID createUser(UserRequest userRequest) {
        var user = userMapper.toDomain(userRequest);
        var savedUser = userRepository.save(user);

        var userCreatedEvent = new UserCreatedEvent(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
        var eventAsJson = objectMapper.writeValueAsString(userCreatedEvent);
        var outboxEvent = OutboxEvent.pending(
            savedUser.getId(),
            "UserCreated",
            "user.created",
            eventAsJson
        );
        outboxRepository.save(outboxEvent);

        return savedUser.getId();
    }
}
