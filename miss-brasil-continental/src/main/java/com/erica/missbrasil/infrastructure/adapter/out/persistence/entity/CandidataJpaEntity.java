package com.erica.missbrasil.infrastructure.adapter.out.persistence.entity;

import com.erica.missbrasil.domain.model.StatusCandidata;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "candidatas")
public class CandidataJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "protocolo", nullable = false, unique = true, length = 50)
    private String protocolo;

    @Column(name = "nome_completo", nullable = false, length = 150)
    private String nomeCompleto;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "whatsapp", nullable = false, length = 30)
    private String whatsapp;

    @Column(name = "logradouro", nullable = false, length = 200)
    private String logradouro;

    @Column(name = "numero", length = 30)
    private String numero;

    @Column(name = "bairro", length = 100)
    private String bairro;

    @Column(name = "cidade", nullable = false, length = 100)
    private String cidade;

    @Column(name = "estado", nullable = false, length = 2)
    private String estado;

    @Column(name = "cep", length = 15)
    private String cep;

    @Column(name = "idade", nullable = false)
    private int idade;

    @Column(name = "altura_metros", nullable = false)
    private double alturaMetros;

    @Column(name = "peso_kg", nullable = false)
    private double pesoKg;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "candidata_fotos", joinColumns = @JoinColumn(name = "candidata_id"))
    @Column(name = "foto_url_ou_base64", columnDefinition = "TEXT")
    private List<String> fotos = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusCandidata status;

    @Column(name = "parecer_avaliador", length = 1000)
    private String parecerAvaliador;

    @Column(name = "cadastrada_em", nullable = false, updatable = false)
    private Instant cadastradaEm;

    @Column(name = "avaliada_em")
    private Instant avaliadaEm;

    public CandidataJpaEntity() {}

    public CandidataJpaEntity(
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
        this.id = id;
        this.protocolo = protocolo;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.whatsapp = whatsapp;
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.idade = idade;
        this.alturaMetros = alturaMetros;
        this.pesoKg = pesoKg;
        this.fotos = fotos != null ? new ArrayList<>(fotos) : new ArrayList<>();
        this.status = status;
        this.parecerAvaliador = parecerAvaliador;
        this.cadastradaEm = cadastradaEm;
        this.avaliadaEm = avaliadaEm;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getProtocolo() { return protocolo; }
    public void setProtocolo(String protocolo) { this.protocolo = protocolo; }
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getWhatsapp() { return whatsapp; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }
    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }
    public double getAlturaMetros() { return alturaMetros; }
    public void setAlturaMetros(double alturaMetros) { this.alturaMetros = alturaMetros; }
    public double getPesoKg() { return pesoKg; }
    public void setPesoKg(double pesoKg) { this.pesoKg = pesoKg; }
    public List<String> getFotos() { return fotos; }
    public void setFotos(List<String> fotos) { this.fotos = fotos; }
    public StatusCandidata getStatus() { return status; }
    public void setStatus(StatusCandidata status) { this.status = status; }
    public String getParecerAvaliador() { return parecerAvaliador; }
    public void setParecerAvaliador(String parecerAvaliador) { this.parecerAvaliador = parecerAvaliador; }
    public Instant getCadastradaEm() { return cadastradaEm; }
    public void setCadastradaEm(Instant cadastradaEm) { this.cadastradaEm = cadastradaEm; }
    public Instant getAvaliadaEm() { return avaliadaEm; }
    public void setAvaliadaEm(Instant avaliadaEm) { this.avaliadaEm = avaliadaEm; }
}
