package com.erica.order.infrastructure.adapter.in.messaging;

import com.erica.order.application.port.out.IdempotencyRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;

@Component
public class PedidoFaturadoConsumer {

    private static final Logger log = LoggerFactory.getLogger(PedidoFaturadoConsumer.class);

    private final IdempotencyRepositoryPort idempotencyRepository;

    public PedidoFaturadoConsumer(IdempotencyRepositoryPort idempotencyRepository) {
        this.idempotencyRepository = Objects.requireNonNull(idempotencyRepository, "IdempotencyRepositoryPort é obrigatório");
    }

    @KafkaListener(
        topics = "billing.pedido-faturado",
        groupId = "order-service-group"
    )
    @Transactional
    public void consumir(
        @Payload String mensagem,
        @Header(KafkaHeaders.RECEIVED_KEY) String messageKey,
        @Header(value = "correlationId", required = false) String correlationId
    ) {
        String chaveDeduplicacao = messageKey != null ? messageKey : correlationId;
        if (chaveDeduplicacao == null || chaveDeduplicacao.isBlank()) {
            chaveDeduplicacao = "hash-" + mensagem.hashCode();
        }

        if (idempotencyRepository.jaProcessado(chaveDeduplicacao)) {
            log.warn("Mensagem duplicada descartada pelo consumidor idempotente: chave={}", chaveDeduplicacao);
            return;
        }

        log.info("Processando evento de faturamento recebido: chave={}, payload={}", chaveDeduplicacao, mensagem);

        // Processa lógica de negócio...

        // Registra o processamento para garantir idempotência
        idempotencyRepository.registrarProcessamento(chaveDeduplicacao, Instant.now());
    }
}
