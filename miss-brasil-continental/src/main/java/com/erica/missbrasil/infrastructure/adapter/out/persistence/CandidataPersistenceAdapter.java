package com.erica.missbrasil.infrastructure.adapter.out.persistence;

import com.erica.missbrasil.application.port.out.CandidataRepositoryPort;
import com.erica.missbrasil.domain.model.Candidata;
import com.erica.missbrasil.domain.model.Contato;
import com.erica.missbrasil.domain.model.Endereco;
import com.erica.missbrasil.domain.model.Medidas;
import com.erica.missbrasil.domain.model.StatusCandidata;
import com.erica.missbrasil.infrastructure.adapter.out.persistence.entity.CandidataJpaEntity;
import com.erica.missbrasil.infrastructure.adapter.out.persistence.repository.SpringDataCandidataRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class CandidataPersistenceAdapter implements CandidataRepositoryPort {

    private final SpringDataCandidataRepository repository;

    public CandidataPersistenceAdapter(SpringDataCandidataRepository repository) {
        this.repository = Objects.requireNonNull(repository, "SpringDataCandidataRepository é obrigatório");
    }

    @Override
    public Candidata salvar(Candidata c) {
        CandidataJpaEntity entity = new CandidataJpaEntity(
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

        CandidataJpaEntity salva = repository.save(entity);
        return mapearParaDominio(salva);
    }

    @Override
    public Optional<Candidata> buscarPorId(UUID id) {
        return repository.findById(id).map(this::mapearParaDominio);
    }

    @Override
    public Optional<Candidata> buscarPorProtocolo(String protocolo) {
        return repository.findByProtocolo(protocolo).map(this::mapearParaDominio);
    }

    @Override
    public Optional<Candidata> buscarPorEmail(String email) {
        return repository.findByEmailIgnoreCase(email).map(this::mapearParaDominio);
    }

    @Override
    public List<Candidata> listar(StatusCandidata filtroStatus, String termoBusca) {
        String buscaNormalizada = termoBusca != null && !termoBusca.isBlank() ? termoBusca.trim() : null;
        return repository.pesquisar(filtroStatus, buscaNormalizada).stream()
            .map(this::mapearParaDominio)
            .toList();
    }

    @Override
    public long contarPorStatus(StatusCandidata status) {
        return repository.countByStatus(status);
    }

    @Override
    public long contarTotal() {
        return repository.count();
    }

    private Candidata mapearParaDominio(CandidataJpaEntity e) {
        Contato contato = new Contato(e.getEmail(), e.getWhatsapp());
        Endereco endereco = new Endereco(
            e.getLogradouro(),
            e.getNumero(),
            e.getBairro(),
            e.getCidade(),
            e.getEstado(),
            e.getCep()
        );
        Medidas medidas = new Medidas(e.getIdade(), e.getAlturaMetros(), e.getPesoKg());

        return new Candidata(
            e.getId(),
            e.getProtocolo(),
            e.getNomeCompleto(),
            contato,
            endereco,
            medidas,
            e.getFotos(),
            e.getStatus(),
            e.getParecerAvaliador(),
            e.getCadastradaEm(),
            e.getAvaliadaEm()
        );
    }
}
