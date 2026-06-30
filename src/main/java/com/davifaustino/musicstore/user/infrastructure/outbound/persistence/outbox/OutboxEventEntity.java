package com.davifaustino.musicstore.user.infrastructure.outbound.persistence.outbox;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import com.davifaustino.musicstore.user.application.outbox.OutboxStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_outbox_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEventEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;
    private UUID correlationId;
    private String aggregateType;
    private UUID aggregateId;
    private String eventType;
    private String routingKey;
    private String payload;
    private OutboxStatus status;
    private Integer attempts;
    private Instant occurredAt;
    private Instant publishedAt;
}
