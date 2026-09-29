package com.erica.order.domain.model;

import com.erica.order.domain.exception.DomainValidationException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Pedido {

    private final UUID id;
    private final UUID clienteId;
    private final List<ItemPedido> itens;
    private Dinheiro valorTotal;
    private StatusPedido status;
    private final Instant criadoEm;
    private Instant atualizadoEm;

    public Pedido(UUID id, UUID clienteId, List<ItemPedido> itens, Dinheiro valorTotal, StatusPedido status, Instant criadoEm, Instant atualizadoEm) {
        this.id = Objects.requireNonNull(id, "ID do pedido é obrigatório");
        this.clienteId = Objects.requireNonNull(clienteId, "ID do cliente é obrigatório");
        if (itens == null || itens.isEmpty()) {
            throw new DomainValidationException("O pedido deve conter pelo menos um item");
        }
        this.itens = new ArrayList<>(itens);
        this.valorTotal = Objects.requireNonNull(valorTotal, "Valor total é obrigatório");
        this.status = Objects.requireNonNull(status, "Status é obrigatório");
        this.criadoEm = Objects.requireNonNull(criadoEm, "Data de criação é obrigatória");
        this.atualizadoEm = Objects.requireNonNull(atualizadoEm, "Data de atualização é obrigatória");
    }

    public static Pedido criar(UUID clienteId, List<ItemPedido> itens) {
        UUID novoId = UUID.randomUUID();
        Instant agora = Instant.now();

        if (itens == null || itens.isEmpty()) {
            throw new DomainValidationException("Não é possível criar um pedido sem itens");
        }

        Dinheiro totalCalculado = Dinheiro.ZERO;
        for (ItemPedido item : itens) {
            totalCalculado = totalCalculado.somar(item.calcularSubtotal());
        }

        return new Pedido(
            novoId,
            clienteId,
            itens,
            totalCalculado,
            StatusPedido.CRIADO,
            agora,
            agora
        );
    }

    public void marcarComoPago() {
        if (this.status != StatusPedido.CRIADO && this.status != StatusPedido.PROCESSANDO) {
            throw new DomainValidationException("Pedido no status " + this.status + " não pode ser marcado como PAGO");
        }
        this.status = StatusPedido.PAGO;
        this.atualizadoEm = Instant.now();
    }

    public void faturar() {
        if (this.status != StatusPedido.PAGO) {
            throw new DomainValidationException("Apenas pedidos pagos podem ser faturados");
        }
        this.status = StatusPedido.FATURADO;
        this.atualizadoEm = Instant.now();
    }

    public void cancelar(String motivo) {
        if (this.status == StatusPedido.FATURADO) {
            throw new DomainValidationException("Pedido já faturado não pode ser cancelado diretamente");
        }
        this.status = StatusPedido.CANCELADO;
        this.atualizadoEm = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public Dinheiro getValorTotal() {
        return valorTotal;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public Instant getAtualizadoEm() {
        return atualizadoEm;
    }
}
