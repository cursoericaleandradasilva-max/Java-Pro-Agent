package com.erica.order.application.usecase;

import com.erica.order.application.port.in.CriarPedidoCommand;
import com.erica.order.application.port.out.OutboxRepositoryPort;
import com.erica.order.application.port.out.PedidoRepositoryPort;
import com.erica.order.domain.model.Pedido;
import com.erica.order.domain.model.StatusPedido;
import com.erica.order.infrastructure.outbox.OutboxEventRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriarPedidoServiceTest {

    @Mock
    private PedidoRepositoryPort pedidoRepository;

    @Mock
    private OutboxRepositoryPort outboxRepository;

    private ObjectMapper objectMapper;
    private CriarPedidoService service;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        service = new CriarPedidoService(pedidoRepository, outboxRepository, objectMapper);
    }

    @Test
    @DisplayName("Deve criar pedido com sucesso e gravar registro atômico na tabela outbox")
    void deveCriarPedidoESalvarNaOutbox() {
        // Given
        UUID clienteId = UUID.randomUUID();
        UUID produtoId = UUID.randomUUID();

        var itemCommand = new CriarPedidoCommand.ItemCommand(
            produtoId,
            "Smartphone Pro Max",
            2,
            BigDecimal.valueOf(1500.00)
        );

        var command = new CriarPedidoCommand(clienteId, List.of(itemCommand));

        when(pedidoRepository.salvar(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Pedido pedidoResultado = service.executar(command);

        // Then
        assertThat(pedidoResultado).isNotNull();
        assertThat(pedidoResultado.getClienteId()).isEqualTo(clienteId);
        assertThat(pedidoResultado.getStatus()).isEqualTo(StatusPedido.CRIADO);
        assertThat(pedidoResultado.getValorTotal().valor()).isEqualByComparingTo(BigDecimal.valueOf(3000.00));

        // Verifica se o repositório de domínio foi acionado
        verify(pedidoRepository).salvar(any(Pedido.class));

        // Verifica se o evento foi gravado na Outbox na mesma transação
        ArgumentCaptor<OutboxEventRecord> outboxCaptor = ArgumentCaptor.forClass(OutboxEventRecord.class);
        verify(outboxRepository).salvar(outboxCaptor.capture());

        OutboxEventRecord outboxSalvo = outboxCaptor.getValue();
        assertThat(outboxSalvo.aggregateType()).isEqualTo("Pedido");
        assertThat(outboxSalvo.aggregateId()).isEqualTo(pedidoResultado.getId().toString());
        assertThat(outboxSalvo.eventType()).isEqualTo("PedidoCriadoEvent");
        assertThat(outboxSalvo.status()).isEqualTo(OutboxEventRecord.OutboxStatus.PENDING);
        assertThat(outboxSalvo.payload()).contains("3000.00");
    }
}
