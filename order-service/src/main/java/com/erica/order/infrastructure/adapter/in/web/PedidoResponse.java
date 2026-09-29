package com.erica.order.infrastructure.adapter.in.web;

import com.erica.order.domain.model.Pedido;
import com.erica.order.domain.model.StatusPedido;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record PedidoResponse(
    UUID id,
    UUID clienteId,
    BigDecimal valorTotal,
    StatusPedido status,
    List<ItemResponse> itens,
    Instant criadoEm
) {
    public record ItemResponse(
        UUID produtoId,
        String nomeProduto,
        int quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
    ) {}

    public static PedidoResponse aPartirDe(Pedido pedido) {
        List<ItemResponse> itensResponse = pedido.getItens().stream()
            .map(item -> new ItemResponse(
                item.produtoId(),
                item.nomeProduto(),
                item.quantidade(),
                item.precoUnitario().valor(),
                item.calcularSubtotal().valor()
            ))
            .toList();

        return new PedidoResponse(
            pedido.getId(),
            pedido.getClienteId(),
            pedido.getValorTotal().valor(),
            pedido.getStatus(),
            itensResponse,
            pedido.getCriadoEm()
        );
    }
}
