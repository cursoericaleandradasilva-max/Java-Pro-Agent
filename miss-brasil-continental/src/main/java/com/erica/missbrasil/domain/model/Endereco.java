package com.erica.missbrasil.domain.model;

import com.erica.missbrasil.domain.exception.RegraNegocioException;
import java.util.Objects;

public record Endereco(
    String logradouro,
    String numero,
    String bairro,
    String cidade,
    String estado,
    String cep
) {
    public Endereco {
        Objects.requireNonNull(logradouro, "Logradouro é obrigatório");
        Objects.requireNonNull(cidade, "Cidade é obrigatória");
        Objects.requireNonNull(estado, "Estado é obrigatório");

        if (logradouro.isBlank()) throw new RegraNegocioException("Logradouro não pode ser vazio");
        if (cidade.isBlank()) throw new RegraNegocioException("Cidade não pode ser vazia");
        if (estado.isBlank() || estado.trim().length() < 2) throw new RegraNegocioException("Estado inválido");

        logradouro = logradouro.trim();
        cidade = cidade.trim();
        estado = estado.trim().toUpperCase();
        numero = numero != null ? numero.trim() : "S/N";
        bairro = bairro != null ? bairro.trim() : "";
        cep = cep != null ? cep.trim() : "";
    }
}
