package com.erica.order.domain.model;

import com.erica.order.domain.exception.DomainValidationException;
import java.util.Objects;
import java.util.UUID;

public record ItemPedido(
    UUID produtoId,
    String nomeProduto,
    int quantidade,
    Dinheiro precoUnitario
) {
    public ItemPedido {
        Objects.requireNonNull(produtoId, "ID do produto é obrigatório");
        Objects.requireNonNull(nomeProduto, "Nome do produto é obrigatório");
        Objects.requireNonNull(precoUnitario, "Preço unitário é obrigatório");

        if (nomeProduto.isBlank()) {
            throw new DomainValidationException("Nome do produto não pode ser vazio");
        }
        if (quantidade <= 0) {
            throw new DomainValidationException("Quantidade deve ser maior que zero");
        }
    }

    public Dinheiro calcularSubtotal() {
        return precoUnitario.multiplicar(quantidade);
    }
}
