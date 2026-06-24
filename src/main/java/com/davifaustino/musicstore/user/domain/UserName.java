package com.davifaustino.musicstore.user.domain;

public record UserName(String value) {

    public UserName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("User name is required");
        }

        value = value.trim();
    }
}
