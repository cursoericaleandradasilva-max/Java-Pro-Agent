package com.erica.order.domain.model;

import com.erica.order.domain.exception.DomainValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PedidoTest {

    @Test
    @DisplayName("Deve calcular corretamente o valor total do pedido e inicializar com status CRIADO")
    void deveCalcularValorTotalCorretamente() {
        UUID clienteId = UUID.randomUUID();
        ItemPedido item1 = new ItemPedido(UUID.randomUUID(), "Teclado Mecânico", 2, Dinheiro.de(150.00));
        ItemPedido item2 = new ItemPedido(UUID.randomUUID(), "Mouse Gamer", 1, Dinheiro.de(200.00));

        Pedido pedido = Pedido.criar(clienteId, List.of(item1, item2));

        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.CRIADO);
        assertThat(pedido.getValorTotal().valor()).isEqualByComparingTo(BigDecimal.valueOf(500.00));
        assertThat(pedido.getItens()).hasSize(2);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar pedido sem itens")
    void deveLancarExcecaoSemItens() {
        UUID clienteId = UUID.randomUUID();
        List<ItemPedido> itensVazios = Collections.emptyList();

        assertThatThrownBy(() -> Pedido.criar(clienteId, itensVazios))
            .isInstanceOf(DomainValidationException.class)
            .hasMessageContaining("Não é possível criar um pedido sem itens");
    }

    @Test
    @DisplayName("Deve transitar status de CRIADO para PAGO e depois para FATURADO")
    void deveTransitarStatusCorretamente() {
        UUID clienteId = UUID.randomUUID();
        ItemPedido item = new ItemPedido(UUID.randomUUID(), "Monitor 4K", 1, Dinheiro.de(2500.00));
        Pedido pedido = Pedido.criar(clienteId, List.of(item));

        pedido.marcarComoPago();
        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.PAGO);

        pedido.faturar();
        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.FATURADO);
    }
}
