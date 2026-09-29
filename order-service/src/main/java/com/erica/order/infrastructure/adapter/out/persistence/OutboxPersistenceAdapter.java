package com.erica.order.infrastructure.adapter.out.persistence;

import com.erica.order.application.port.out.OutboxRepositoryPort;
import com.erica.order.infrastructure.adapter.out.persistence.entity.OutboxJpaEntity;
import com.erica.order.infrastructure.adapter.out.persistence.repository.SpringDataOutboxRepository;
import com.erica.order.infrastructure.outbox.OutboxEventRecord;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class OutboxPersistenceAdapter implements OutboxRepositoryPort {

    private final SpringDataOutboxRepository repository;

    public OutboxPersistenceAdapter(SpringDataOutboxRepository repository) {
        this.repository = Objects.requireNonNull(repository, "SpringDataOutboxRepository é obrigatório");
    }

    @Override
    public void salvar(OutboxEventRecord evento) {
        OutboxJpaEntity entity = new OutboxJpaEntity(
            evento.id(),
            evento.aggregateType(),
            evento.aggregateId(),
            evento.eventType(),
            evento.payload(),
            evento.createdAt(),
            evento.status()
        );
        repository.save(entity);
    }

    @Override
    public List<OutboxEventRecord> buscarPendentesComLock(int limite) {
        return repository.findByStatusOrderByCreatedAtAsc(
            OutboxEventRecord.OutboxStatus.PENDING,
            PageRequest.of(0, limite)
        ).stream()
            .map(entity -> new OutboxEventRecord(
                entity.getId(),
                entity.getAggregateType(),
                entity.getAggregateId(),
                entity.getEventType(),
                entity.getPayload(),
                entity.getCreatedAt(),
                entity.getStatus(),
                entity.getErrorMessage()
            ))
            .toList();
    }

    @Override
    @Transactional
    public void marcarComoPublicado(UUID id, Instant publicadoEm) {
        repository.findById(id).ifPresent(entity -> {
            entity.setStatus(OutboxEventRecord.OutboxStatus.PUBLISHED);
            entity.setPublishedAt(publicadoEm);
            entity.setErrorMessage(null);
            repository.save(entity);
        });
    }

    @Override
    @Transactional
    public void marcarComoFalha(UUID id, String motivo) {
        repository.findById(id).ifPresent(entity -> {
            entity.setStatus(OutboxEventRecord.OutboxStatus.FAILED);
            entity.setErrorMessage(motivo != null && motivo.length() > 950 ? motivo.substring(0, 950) : motivo);
            repository.save(entity);
        });
    }
}
