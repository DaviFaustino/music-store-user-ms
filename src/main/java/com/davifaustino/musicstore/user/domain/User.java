package com.davifaustino.musicstore.user.domain;

import java.util.UUID;

public class User {

    private UUID id;
    private UserName name;
    private EmailAddress email;

    public User() {
    }

    public User(UUID id, String name, String email) {
        this.id = id;
        this.name = new UserName(name);
        this.email = new EmailAddress(email);
    }

    public User(UUID id, UserName name, EmailAddress email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name.value();
    }

    public void setName(String name) {
        this.name = new UserName(name);
    }

    public String getEmail() {
        return email.value();
    }

    public void setEmail(String email) {
        this.email = new EmailAddress(email);
    }
}
