package com.erica.order.application.port.in;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CriarPedidoCommand(
    UUID clienteId,
    List<ItemCommand> itens
) {
    public record ItemCommand(
        UUID produtoId,
        String nomeProduto,
        int quantidade,
        BigDecimal precoUnitario
    ) {}
}
