package com.erica.missbrasil.application.usecase;

import com.erica.missbrasil.application.port.in.ConsultarCandidatasUseCase;
import com.erica.missbrasil.application.port.out.CandidataRepositoryPort;
import com.erica.missbrasil.domain.model.Candidata;
import com.erica.missbrasil.domain.model.StatusCandidata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConsultarCandidatasService implements ConsultarCandidatasUseCase {

    private final CandidataRepositoryPort repository;

    public ConsultarCandidatasService(CandidataRepositoryPort repository) {
        this.repository = Objects.requireNonNull(repository, "CandidataRepositoryPort é obrigatório");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Candidata> listarTodas(StatusCandidata filtroStatus, String buscaNomeOuEstado) {
        return repository.listar(filtroStatus, buscaNomeOuEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Candidata> buscarPorId(UUID id) {
        Objects.requireNonNull(id, "ID não pode ser nulo");
        return repository.buscarPorId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Candidata> buscarPorProtocolo(String protocolo) {
        if (protocolo == null || protocolo.isBlank()) {
            return Optional.empty();
        }
        return repository.buscarPorProtocolo(protocolo.trim().toUpperCase());
    }

    @Transactional(readOnly = true)
    public ResumoContagem obterResumo() {
        long total = repository.contarTotal();
        long emAnalise = repository.contarPorStatus(StatusCandidata.EM_ANALISE);
        long aprovadas = repository.contarPorStatus(StatusCandidata.APROVADA);
        long reprovadas = repository.contarPorStatus(StatusCandidata.REPROVADA);
        return new ResumoContagem(total, emAnalise, aprovadas, reprovadas);
    }

    public record ResumoContagem(
        long total,
        long emAnalise,
        long aprovadas,
        long reprovadas
    ) {}
}
