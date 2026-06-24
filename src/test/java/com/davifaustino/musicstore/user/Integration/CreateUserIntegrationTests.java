package com.davifaustino.musicstore.user.Integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.davifaustino.musicstore.user.IntegrationTests;
import com.davifaustino.musicstore.user.application.outbox.OutboxStatus;
import com.davifaustino.musicstore.user.infrastructure.outbound.persistence.JpaUserRepository;
import com.davifaustino.musicstore.user.infrastructure.outbound.persistence.outbox.JpaOutboxRepository;

public class CreateUserIntegrationTests extends IntegrationTests {

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JpaOutboxRepository jpaOutboxRepository;

    @BeforeEach
    void cleanDatabase() {
        jpaOutboxRepository.deleteAll();
        jpaUserRepository.deleteAll();
    }

    @Test
    void shouldCreateUser() throws Exception {
        var name = "Davi Faustino";
        var email = "davi@musicstore.com";

        var response = postJson("/users", userRequestJson(name, email));
        var createdUserId = parseUuidResponse(response.body());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        var savedUser = jpaUserRepository.findById(createdUserId);

        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getName()).isEqualTo(name);
        assertThat(savedUser.get().getEmail()).isEqualTo(email);
    }

    @Test
    void shouldCreateOutboxEventWhenUserIsCreated() throws Exception {
        var name = "Davi Faustino";
        var email = "davi@musicstore.com";

        var response = postJson("/users", userRequestJson(name, email));
        var createdUserId = parseUuidResponse(response.body());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        var outboxEvents = jpaOutboxRepository.findAll();

        assertThat(outboxEvents).hasSize(1);

        var outboxEvent = outboxEvents.getFirst();
        assertThat(outboxEvent.getCorrelationId()).isEqualTo(createdUserId);
        assertThat(outboxEvent.getEventType()).isEqualTo("UserCreated");
        assertThat(outboxEvent.getRoutingKey()).isEqualTo("user.created");
        assertThat(outboxEvent.getPayload()).contains(
                createdUserId.toString(),
                name,
                email);
        assertThat(outboxEvent.getStatus()).isIn(OutboxStatus.PENDING, OutboxStatus.PROCESSED);
        assertThat(outboxEvent.getAttempts()).isGreaterThanOrEqualTo(0);
        assertThat(outboxEvent.getOccurredAt()).isNotNull();
    }

    @Test
    void shouldReturnJsonWithCreatedUserId() throws Exception {
        var response = postJson("/users", userRequestJson("Davi Faustino", "davi@musicstore.com"));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.headers().firstValue(HttpHeaders.CONTENT_TYPE))
                .hasValueSatisfying(contentType -> assertThat(contentType).contains(MediaType.APPLICATION_JSON_VALUE));
        assertThat(parseUuidResponse(response.body())).isNotNull();
    }

    @Test
    void shouldReturnBadRequestWhenNameIsMissing() throws Exception {
        var response = postJson("/users", """
                {
                    "email": "davi@musicstore.com"
                }
                """);

        assertBadRequestWithoutPersistence(response.statusCode());
    }

    @Test
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        var response = postJson("/users", userRequestJson(" ", "davi@musicstore.com"));

        assertBadRequestWithoutPersistence(response.statusCode());
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsMissing() throws Exception {
        var response = postJson("/users", """
                {
                    "name": "Davi Faustino"
                }
                """);

        assertBadRequestWithoutPersistence(response.statusCode());
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsBlank() throws Exception {
        var response = postJson("/users", userRequestJson("Davi Faustino", " "));

        assertBadRequestWithoutPersistence(response.statusCode());
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        var response = postJson("/users", userRequestJson("Davi Faustino", "not-an-email"));

        assertBadRequestWithoutPersistence(response.statusCode());
    }

    @Test
    void shouldReturnBadRequestWhenRequestBodyIsMalformed() throws Exception {
        var response = postJson("/users", """
                {
                    "name": "Davi Faustino",
                    "email": "davi@musicstore.com"
                """);

        assertBadRequestWithoutPersistence(response.statusCode());
    }

    @Test
    void shouldReturnBadRequestWhenRequestBodyIsEmpty() throws Exception {
        var response = postJson("/users", "");

        assertBadRequestWithoutPersistence(response.statusCode());
    }

    private String userRequestJson(String name, String email) {
        return """
                {
                    "name": "%s",
                    "email": "%s"
                }
                """.formatted(name, email);
    }

    private UUID parseUuidResponse(String value) {
        return UUID.fromString(value.replace("\"", ""));
    }

    private void assertBadRequestWithoutPersistence(int statusCode) {
        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(jpaUserRepository.findAll()).isEmpty();
        assertThat(jpaOutboxRepository.findAll()).isEmpty();
    }
}
