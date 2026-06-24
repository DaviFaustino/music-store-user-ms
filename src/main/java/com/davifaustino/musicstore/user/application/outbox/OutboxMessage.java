package com.davifaustino.musicstore.user.application.outbox;

import java.util.UUID;

public record OutboxMessage(
    UUID id,
    UUID correlationId,
    String eventType,
    String payload) {
}
