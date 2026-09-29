package com.erica.order.application.port.in;

import com.erica.order.domain.model.Pedido;

public interface CriarPedidoUseCase {
    Pedido executar(CriarPedidoCommand command);
}
