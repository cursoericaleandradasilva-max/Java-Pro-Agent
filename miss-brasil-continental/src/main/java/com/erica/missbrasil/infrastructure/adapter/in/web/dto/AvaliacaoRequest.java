package com.erica.missbrasil.infrastructure.adapter.in.web.dto;

import com.erica.missbrasil.domain.model.StatusCandidata;
import jakarta.validation.constraints.NotNull;

public record AvaliacaoRequest(
    @NotNull(message = "O status é obrigatório")
    StatusCandidata status,

    String parecer
) {}
