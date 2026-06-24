package com.davifaustino.musicstore.user.infrastructure.inbound.web;

public record UserRequest(
    String name,
    String email) {
}
