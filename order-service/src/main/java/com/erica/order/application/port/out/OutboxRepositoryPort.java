package com.erica.order.application.port.out;

import com.erica.order.infrastructure.outbox.OutboxEventRecord;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface OutboxRepositoryPort {
    void salvar(OutboxEventRecord evento);
    List<OutboxEventRecord> buscarPendentesComLock(int limite);
    void marcarComoPublicado(UUID id, Instant publicadoEm);
    void marcarComoFalha(UUID id, String motivo);
}
