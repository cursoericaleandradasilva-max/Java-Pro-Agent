package com.erica.missbrasil.infrastructure.adapter.in.web.dto;

import com.erica.missbrasil.domain.model.Candidata;
import com.erica.missbrasil.domain.model.StatusCandidata;

import java.time.Instant;

public record InscricaoResponse(
    String protocolo,
    String nome,
    StatusCandidata status,
    String mensagem,
    Instant dataInscricao
) {
    public static InscricaoResponse aPartirDe(Candidata candidata) {
        return new InscricaoResponse(
            candidata.getProtocolo(),
            candidata.getNomeCompleto(),
            candidata.getStatus(),
            "Inscrição realizada com sucesso! Guarde o seu número de protocolo para acompanhar o status.",
            candidata.getCadastradaEm()
        );
    }
}
