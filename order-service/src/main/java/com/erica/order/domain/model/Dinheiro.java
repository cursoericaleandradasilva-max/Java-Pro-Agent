package com.erica.order.domain.model;

import com.erica.order.domain.exception.DomainValidationException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Dinheiro(BigDecimal valor) {

    public static final Dinheiro ZERO = new Dinheiro(BigDecimal.ZERO);

    public Dinheiro {
        Objects.requireNonNull(valor, "Valor monetário não pode ser nulo");
        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainValidationException("Valor monetário não pode ser negativo");
        }
        valor = valor.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static Dinheiro de(BigDecimal valor) {
        return new Dinheiro(valor);
    }

    public static Dinheiro de(double valor) {
        return new Dinheiro(BigDecimal.valueOf(valor));
    }

    public Dinheiro somar(Dinheiro outro) {
        Objects.requireNonNull(outro, "Valor a somar não pode ser nulo");
        return new Dinheiro(this.valor.add(outro.valor));
    }

    public Dinheiro multiplicar(int quantidade) {
        if (quantidade < 0) {
            throw new DomainValidationException("Quantidade não pode ser negativa");
        }
        return new Dinheiro(this.valor.multiply(BigDecimal.valueOf(quantidade)));
    }
}
