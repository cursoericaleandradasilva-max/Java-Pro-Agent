package com.erica.order.application.port.out;

import java.time.Instant;

public interface IdempotencyRepositoryPort {
    boolean jaProcessado(String messageId);
    void registrarProcessamento(String messageId, Instant processadoEm);
}
