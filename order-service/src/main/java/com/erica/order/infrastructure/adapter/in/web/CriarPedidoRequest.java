package com.erica.order.infrastructure.adapter.in.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CriarPedidoRequest(
    @NotNull(message = "O ID do cliente é obrigatório")
    UUID clienteId,

    @NotEmpty(message = "A lista de itens não pode ser vazia")
    @Valid
    List<ItemRequest> itens
) {
    public record ItemRequest(
        @NotNull(message = "O ID do produto é obrigatório")
        UUID produtoId,

        @NotBlank(message = "O nome do produto é obrigatório")
        String nomeProduto,

        @Positive(message = "A quantidade deve ser maior que zero")
        int quantidade,

        @NotNull(message = "O preço unitário é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço unitário deve ser no mínimo 0.01")
        BigDecimal precoUnitario
    ) {}
}
