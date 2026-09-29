package com.erica.order.application.port.out;

import com.erica.order.domain.model.Pedido;
import java.util.Optional;
import java.util.UUID;

public interface PedidoRepositoryPort {
    Pedido salvar(Pedido pedido);
    Optional<Pedido> buscarPorId(UUID id);
}
