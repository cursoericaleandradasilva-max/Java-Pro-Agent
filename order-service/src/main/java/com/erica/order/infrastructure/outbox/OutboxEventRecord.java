package com.erica.order.infrastructure.outbox;

import java.time.Instant;
import java.util.UUID;

public record OutboxEventRecord(
    UUID id,
    String aggregateType,
    String aggregateId,
    String eventType,
    String payload,
    Instant createdAt,
    OutboxStatus status,
    String errorMessage
) {
    public enum OutboxStatus {
        PENDING,
        PUBLISHED,
        FAILED
    }

    public static OutboxEventRecord criarPendente(
        String aggregateType,
        String aggregateId,
        String eventType,
        String payload
    ) {
        return new OutboxEventRecord(
            UUID.randomUUID(),
            aggregateType,
            aggregateId,
            eventType,
            payload,
            Instant.now(),
            OutboxStatus.PENDING,
            null
        );
    }
}
