package com.davifaustino.musicstore.user.infrastructure.outbound.persistence.outbox;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.davifaustino.musicstore.user.application.outbox.OutboxStatus;

public interface JpaOutboxRepository extends JpaRepository<OutboxEventEntity, UUID> {

    List<OutboxEventEntity> findByStatus(OutboxStatus status);
}
