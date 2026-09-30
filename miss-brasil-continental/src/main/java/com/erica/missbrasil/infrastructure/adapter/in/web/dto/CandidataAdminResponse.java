package com.erica.missbrasil.infrastructure.adapter.in.web.dto;

import com.erica.missbrasil.domain.model.Candidata;
import com.erica.missbrasil.domain.model.StatusCandidata;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CandidataAdminResponse(
    UUID id,
    String protocolo,
    String nomeCompleto,
    String email,
    String whatsapp,
    String logradouro,
    String numero,
    String bairro,
    String cidade,
    String estado,
    String cep,
    int idade,
    double alturaMetros,
    double pesoKg,
    List<String> fotos,
    StatusCandidata status,
    String parecerAvaliador,
    Instant cadastradaEm,
    Instant avaliadaEm
) {
    public static CandidataAdminResponse aPartirDe(Candidata c) {
        return new CandidataAdminResponse(
            c.getId(),
            c.getProtocolo(),
            c.getNomeCompleto(),
            c.getContato().email(),
            c.getContato().whatsapp(),
            c.getEndereco().logradouro(),
            c.getEndereco().numero(),
            c.getEndereco().bairro(),
            c.getEndereco().cidade(),
            c.getEndereco().estado(),
            c.getEndereco().cep(),
            c.getMedidas().idade(),
            c.getMedidas().alturaMetros(),
            c.getMedidas().pesoKg(),
            c.getFotos(),
            c.getStatus(),
            c.getParecerAvaliador(),
            c.getCadastradaEm(),
            c.getAvaliadaEm()
        );
    }
}
