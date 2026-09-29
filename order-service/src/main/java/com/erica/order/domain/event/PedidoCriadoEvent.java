package com.erica.order.domain.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PedidoCriadoEvent(
    UUID eventId,
    UUID pedidoId,
    UUID clienteId,
    BigDecimal valorTotal,
    Instant ocorridoEm
) {
    public PedidoCriadoEvent(UUID pedidoId, UUID clienteId, BigDecimal valorTotal) {
        this(UUID.randomUUID(), pedidoId, clienteId, valorTotal, Instant.now());
    }
}
