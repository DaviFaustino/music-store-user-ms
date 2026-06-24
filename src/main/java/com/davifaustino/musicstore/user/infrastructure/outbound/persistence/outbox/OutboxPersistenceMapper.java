package com.davifaustino.musicstore.user.infrastructure.outbound.persistence.outbox;

import org.springframework.stereotype.Component;

import com.davifaustino.musicstore.user.application.outbox.OutboxEvent;

@Component
public class OutboxPersistenceMapper {

    public static OutboxEventEntity toEntity(OutboxEvent event) {
        var outboxEvent = new OutboxEventEntity();
        outboxEvent.setId(event.getId());
        outboxEvent.setCorrelationId(event.getCorrelationId());
        outboxEvent.setEventType(event.getEventType());
        outboxEvent.setRoutingKey(event.getRoutingKey());
        outboxEvent.setPayload(event.getPayload());
        outboxEvent.setStatus(event.getStatus());
        outboxEvent.setAttempts(event.getAttempts());
        outboxEvent.setOccurredAt(event.getOccurredAt());
        outboxEvent.setPublishedAt(event.getPublishedAt());
        return outboxEvent;
    }

    public static OutboxEvent toDomain(OutboxEventEntity outboxEvent) {
        var event = new OutboxEvent();
        event.setId(outboxEvent.getId());
        event.setCorrelationId(outboxEvent.getCorrelationId());
        event.setEventType(outboxEvent.getEventType());
        event.setRoutingKey(outboxEvent.getRoutingKey());
        event.setPayload(outboxEvent.getPayload());
        event.setStatus(outboxEvent.getStatus());
        event.setAttempts(outboxEvent.getAttempts());
        event.setOccurredAt(outboxEvent.getOccurredAt());
        event.setPublishedAt(outboxEvent.getPublishedAt());
        return event;
    }
}
