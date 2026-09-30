package com.erica.missbrasil.domain.model;

import com.erica.missbrasil.domain.exception.RegraNegocioException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Candidata {

    private final UUID id;
    private final String protocolo;
    private final String nomeCompleto;
    private final Contato contato;
    private final Endereco endereco;
    private final Medidas medidas;
    private final List<String> fotos;
    private StatusCandidata status;
    private String parecerAvaliador;
    private final Instant cadastradaEm;
    private Instant avaliadaEm;

    public Candidata(
        UUID id,
        String protocolo,
        String nomeCompleto,
        Contato contato,
        Endereco endereco,
        Medidas medidas,
        List<String> fotos,
        StatusCandidata status,
        String parecerAvaliador,
        Instant cadastradaEm,
        Instant avaliadaEm
    ) {
        this.id = Objects.requireNonNull(id, "ID é obrigatório");
        this.protocolo = Objects.requireNonNull(protocolo, "Protocolo é obrigatório");
        this.nomeCompleto = Objects.requireNonNull(nomeCompleto, "Nome completo é obrigatório").trim();
        if (this.nomeCompleto.length() < 3) {
            throw new RegraNegocioException("Nome completo deve ter no mínimo 3 caracteres");
        }
        this.contato = Objects.requireNonNull(contato, "Contato é obrigatório");
        this.endereco = Objects.requireNonNull(endereco, "Endereço é obrigatório");
        this.medidas = Objects.requireNonNull(medidas, "Medidas são obrigatórias");
        this.fotos = fotos != null ? new ArrayList<>(fotos) : new ArrayList<>();
        this.status = Objects.requireNonNull(status, "Status é obrigatório");
        this.parecerAvaliador = parecerAvaliador;
        this.cadastradaEm = Objects.requireNonNull(cadastradaEm, "Data de cadastro é obrigatória");
        this.avaliadaEm = avaliadaEm;
    }

    public static Candidata novaInscricao(
        String nomeCompleto,
        Contato contato,
        Endereco endereco,
        Medidas medidas,
        List<String> fotos
    ) {
        UUID novoId = UUID.randomUUID();
        String anoAtual = String.valueOf(java.time.Year.now().getValue());
        String randomSuffix = novoId.toString().substring(0, 8).toUpperCase();
        String protocoloGerado = "MBC-" + anoAtual + "-" + randomSuffix;

        return new Candidata(
            novoId,
            protocoloGerado,
            nomeCompleto,
            contato,
            endereco,
            medidas,
            fotos,
            StatusCandidata.EM_ANALISE,
            null,
            Instant.now(),
            null
        );
    }

    public void aprovar(String parecer) {
        this.status = StatusCandidata.APROVADA;
        this.parecerAvaliador = parecer != null && !parecer.isBlank() ? parecer.trim() : "Candidata aprovada na triagem inicial.";
        this.avaliadaEm = Instant.now();
    }

    public void reprovar(String parecer) {
        this.status = StatusCandidata.REPROVADA;
        this.parecerAvaliador = parecer != null && !parecer.isBlank() ? parecer.trim() : "Candidata reprovada na triagem inicial.";
        this.avaliadaEm = Instant.now();
    }

    public void colocarEmAnalise(String parecer) {
        this.status = StatusCandidata.EM_ANALISE;
        this.parecerAvaliador = parecer != null ? parecer.trim() : null;
        this.avaliadaEm = Instant.now();
    }

    public UUID getId() { return id; }
    public String getProtocolo() { return protocolo; }
    public String getNomeCompleto() { return nomeCompleto; }
    public Contato getContato() { return contato; }
    public Endereco getEndereco() { return endereco; }
    public Medidas getMedidas() { return medidas; }
    public List<String> getFotos() { return Collections.unmodifiableList(fotos); }
    public StatusCandidata getStatus() { return status; }
    public String getParecerAvaliador() { return parecerAvaliador; }
    public Instant getCadastradaEm() { return cadastradaEm; }
    public Instant getAvaliadaEm() { return avaliadaEm; }
}
