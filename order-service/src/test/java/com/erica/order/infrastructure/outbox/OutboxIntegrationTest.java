package com.erica.order.infrastructure.outbox;

import com.erica.order.application.port.in.CriarPedidoCommand;
import com.erica.order.application.port.in.CriarPedidoUseCase;
import com.erica.order.domain.model.Pedido;
import com.erica.order.infrastructure.adapter.out.persistence.entity.OutboxJpaEntity;
import com.erica.order.infrastructure.adapter.out.persistence.repository.SpringDataOutboxRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class OutboxIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
        .withDatabaseName("orderdb_test")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        if (postgres.isRunning()) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl);
            registry.add("spring.datasource.username", postgres::getUsername);
            registry.add("spring.datasource.password", postgres::getPassword);
        }
    }

    @Autowired
    private CriarPedidoUseCase criarPedidoUseCase;

    @Autowired
    private SpringDataOutboxRepository outboxRepository;

    @Test
    @DisplayName("Deve persistir o pedido e registrar evento na tabela outbox com status PENDING no PostgreSQL real")
    void devePersistirPedidoEOutboxComSucesso() {
        UUID clienteId = UUID.randomUUID();
        UUID produtoId = UUID.randomUUID();

        var command = new CriarPedidoCommand(
            clienteId,
            List.of(new CriarPedidoCommand.ItemCommand(
                produtoId,
                "Notebook Dell Ultra",
                1,
                BigDecimal.valueOf(4999.90)
            ))
        );

        Pedido pedido = criarPedidoUseCase.executar(command);

        assertThat(pedido).isNotNull();
        assertThat(pedido.getId()).isNotNull();

        // Busca o evento gravado na outbox no banco de dados
        List<OutboxJpaEntity> eventos = outboxRepository.findAll();
        Optional<OutboxJpaEntity> eventoOutbox = eventos.stream()
            .filter(e -> e.getAggregateId().equals(pedido.getId().toString()))
            .findFirst();

        assertThat(eventoOutbox).isPresent();
        assertThat(eventoOutbox.get().getStatus()).isEqualTo(OutboxEventRecord.OutboxStatus.PENDING);
        assertThat(eventoOutbox.get().getEventType()).isEqualTo("PedidoCriadoEvent");
        assertThat(eventoOutbox.get().getPayload()).contains("4999.90");
    }
}
