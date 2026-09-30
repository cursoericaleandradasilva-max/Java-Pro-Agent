package com.erica.missbrasil.domain.model;

import com.erica.missbrasil.domain.exception.RegraNegocioException;

public record Medidas(
    int idade,
    double alturaMetros,
    double pesoKg
) {
    public Medidas {
        if (idade < 16 || idade > 40) {
            throw new RegraNegocioException("A idade da candidata deve ser entre 16 e 40 anos");
        }
        if (alturaMetros < 1.40 || alturaMetros > 2.20) {
            throw new RegraNegocioException("Altura informada inválida (deve estar entre 1.40m e 2.20m)");
        }
        if (pesoKg < 35.0 || pesoKg > 150.0) {
            throw new RegraNegocioException("Peso informado inválido (deve estar entre 35kg e 150kg)");
        }
    }
}
