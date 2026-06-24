package com.davifaustino.musicstore.user.domain;

import java.util.UUID;

public record UserCreatedEvent(
    UUID userId,
    String name,
    String email) {
}
