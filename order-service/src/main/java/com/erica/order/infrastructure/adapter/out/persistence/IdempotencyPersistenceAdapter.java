package com.erica.order.infrastructure.adapter.out.persistence;

import com.erica.order.application.port.out.IdempotencyRepositoryPort;
import com.erica.order.infrastructure.adapter.out.persistence.entity.IdempotencyKeyJpaEntity;
import com.erica.order.infrastructure.adapter.out.persistence.repository.SpringDataIdempotencyRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;

@Component
public class IdempotencyPersistenceAdapter implements IdempotencyRepositoryPort {

    private final SpringDataIdempotencyRepository repository;

    public IdempotencyPersistenceAdapter(SpringDataIdempotencyRepository repository) {
        this.repository = Objects.requireNonNull(repository, "SpringDataIdempotencyRepository é obrigatório");
    }

    @Override
    public boolean jaProcessado(String messageKey) {
        return repository.existsById(messageKey);
    }

    @Override
    @Transactional
    public void registrarProcessamento(String messageKey, Instant processadoEm) {
        repository.save(new IdempotencyKeyJpaEntity(messageKey, processadoEm));
    }
}
