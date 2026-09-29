package com.erica.order.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "idempotency_keys")
public class IdempotencyKeyJpaEntity {

    @Id
    @Column(name = "message_key", nullable = false, length = 150)
    private String messageKey;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt;

    public IdempotencyKeyJpaEntity() {}

    public IdempotencyKeyJpaEntity(String messageKey, Instant processedAt) {
        this.messageKey = messageKey;
        this.processedAt = processedAt;
    }

    public String getMessageKey() { return messageKey; }
    public void setMessageKey(String messageKey) { this.messageKey = messageKey; }
    public Instant getProcessedAt() { return processedAt; }
    public void setProcessedAt(Instant processedAt) { this.processedAt = processedAt; }
}
