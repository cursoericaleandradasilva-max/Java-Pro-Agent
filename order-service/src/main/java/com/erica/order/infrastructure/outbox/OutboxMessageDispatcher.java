package com.erica.order.infrastructure.outbox;

import com.erica.order.application.port.out.MessageBrokerPublisherPort;
import com.erica.order.application.port.out.OutboxRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;

@Component
public class OutboxMessageDispatcher {

    private static final Logger log = LoggerFactory.getLogger(OutboxMessageDispatcher.class);

    private final OutboxRepositoryPort outboxRepository;
    private final MessageBrokerPublisherPort brokerPublisher;

    public OutboxMessageDispatcher(
        OutboxRepositoryPort outboxRepository,
        MessageBrokerPublisherPort brokerPublisher
    ) {
        this.outboxRepository = outboxRepository;
        this.brokerPublisher = brokerPublisher;
    }

    @Scheduled(fixedDelay = 1000)
    public void despacharEventosPendentes() {
        List<OutboxEventRecord> pendentes = outboxRepository.buscarPendentesComLock(50);
        if (pendentes.isEmpty()) {
            return;
        }

        log.debug("Processando {} eventos pendentes na outbox via Virtual Threads...", pendentes.size());

        // Java 21 Project Loom: Executor de Virtual Threads para I/O-bound sem bloqueio
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (OutboxEventRecord evento : pendentes) {
                executor.submit(() -> despacharEvento(evento));
            }
        }
    }

    private void despacharEvento(OutboxEventRecord evento) {
        try {
            brokerPublisher.publicar(
                "orders." + evento.eventType(),
                evento.aggregateId(),
                evento.payload()
            );

            outboxRepository.marcarComoPublicado(evento.id(), Instant.now());
            log.info("Evento outbox despachado com sucesso [id={}, tipo={}, aggregateId={}]",
                evento.id(), evento.eventType(), evento.aggregateId());

        } catch (Exception ex) {
            log.error("Erro ao despachar evento outbox [id={}, tipo={}]: {}",
                evento.id(), evento.eventType(), ex.getMessage(), ex);
            outboxRepository.marcarComoFalha(evento.id(), ex.getMessage());
        }
    }
}
