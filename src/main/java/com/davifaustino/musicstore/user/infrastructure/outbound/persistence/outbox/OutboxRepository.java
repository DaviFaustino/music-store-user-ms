package com.davifaustino.musicstore.user.infrastructure.outbound.persistence.outbox;

import java.util.List;

import org.springframework.stereotype.Component;

import com.davifaustino.musicstore.user.application.outbox.OutboxEvent;
import com.davifaustino.musicstore.user.application.outbox.OutboxStatus;

@Component
public class OutboxRepository {

    private final JpaOutboxRepository jpaOutboxRepository;

    public OutboxRepository(JpaOutboxRepository jpaOutboxRepository) {
        this.jpaOutboxRepository = jpaOutboxRepository;
    }

    public OutboxEvent save(OutboxEvent event) {
        var outboxEvent = OutboxPersistenceMapper.toEntity(event);
        var savedOutboxEvent = jpaOutboxRepository.save(outboxEvent);
        return OutboxPersistenceMapper.toDomain(savedOutboxEvent);
    }

    public List<OutboxEvent> getByStatus(OutboxStatus eventStatus) {
        return jpaOutboxRepository.findByStatus(eventStatus).stream()
                .map(OutboxPersistenceMapper::toDomain)
                .toList();
    }
}
