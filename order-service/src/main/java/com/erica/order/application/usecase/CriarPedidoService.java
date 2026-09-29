package com.erica.order.application.usecase;

import com.erica.order.application.port.in.CriarPedidoCommand;
import com.erica.order.application.port.in.CriarPedidoUseCase;
import com.erica.order.application.port.out.OutboxRepositoryPort;
import com.erica.order.application.port.out.PedidoRepositoryPort;
import com.erica.order.domain.event.PedidoCriadoEvent;
import com.erica.order.domain.model.Dinheiro;
import com.erica.order.domain.model.ItemPedido;
import com.erica.order.domain.model.Pedido;
import com.erica.order.infrastructure.outbox.OutboxEventRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CriarPedidoService implements CriarPedidoUseCase {

    private final PedidoRepositoryPort pedidoRepository;
    private final OutboxRepositoryPort outboxRepository;
    private final ObjectMapper objectMapper;

    public CriarPedidoService(
        PedidoRepositoryPort pedidoRepository,
        OutboxRepositoryPort outboxRepository,
        ObjectMapper objectMapper
    ) {
        this.pedidoRepository = Objects.requireNonNull(pedidoRepository, "PedidoRepositoryPort é obrigatório");
        this.outboxRepository = Objects.requireNonNull(outboxRepository, "OutboxRepositoryPort é obrigatório");
        this.objectMapper = Objects.requireNonNull(objectMapper, "ObjectMapper é obrigatório");
    }

    @Override
    @Transactional
    public Pedido executar(CriarPedidoCommand command) {
        Objects.requireNonNull(command, "Command não pode ser nulo");

        List<ItemPedido> itens = command.itens().stream()
            .map(item -> new ItemPedido(
                item.produtoId(),
                item.nomeProduto(),
                item.quantidade(),
                Dinheiro.de(item.precoUnitario())
            ))
            .toList();

        // 1. Cria a entidade de domínio e calcula regras invariantes
        Pedido pedido = Pedido.criar(command.clienteId(), itens);

        // 2. Persiste a entidade no banco
        Pedido pedidoSalvo = pedidoRepository.salvar(pedido);

        // 3. Cria evento de domínio
        PedidoCriadoEvent evento = new PedidoCriadoEvent(
            pedidoSalvo.getId(),
            pedidoSalvo.getClienteId(),
            pedidoSalvo.getValorTotal().valor()
        );

        // 4. Serializa e salva na tabela outbox na MESMA transação ACID
        String payloadJson = serializarParaJson(evento);
        OutboxEventRecord outbox = OutboxEventRecord.criarPendente(
            "Pedido",
            pedidoSalvo.getId().toString(),
            "PedidoCriadoEvent",
            payloadJson
        );

        outboxRepository.salvar(outbox);

        return pedidoSalvo;
    }

    private String serializarParaJson(Object objeto) {
        try {
            return objectMapper.writeValueAsString(objeto);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Falha ao serializar evento de domínio para a tabela outbox", e);
        }
    }
}
