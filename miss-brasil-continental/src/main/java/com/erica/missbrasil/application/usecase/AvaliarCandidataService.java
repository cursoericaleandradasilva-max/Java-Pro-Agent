package com.erica.missbrasil.application.usecase;

import com.erica.missbrasil.application.port.in.AvaliarCandidataUseCase;
import com.erica.missbrasil.application.port.out.CandidataRepositoryPort;
import com.erica.missbrasil.domain.exception.RegraNegocioException;
import com.erica.missbrasil.domain.model.Candidata;
import com.erica.missbrasil.domain.model.StatusCandidata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
public class AvaliarCandidataService implements AvaliarCandidataUseCase {

    private final CandidataRepositoryPort repository;

    public AvaliarCandidataService(CandidataRepositoryPort repository) {
        this.repository = Objects.requireNonNull(repository, "CandidataRepositoryPort é obrigatório");
    }

    @Override
    @Transactional
    public Candidata executar(UUID candidataId, StatusCandidata novoStatus, String parecer) {
        Objects.requireNonNull(candidataId, "ID da candidata é obrigatório");
        Objects.requireNonNull(novoStatus, "Novo status é obrigatório");

        Candidata candidata = repository.buscarPorId(candidataId)
            .orElseThrow(() -> new RegraNegocioException("Candidata não encontrada com o ID: " + candidataId));

        switch (novoStatus) {
            case APROVADA -> candidata.aprovar(parecer);
            case REPROVADA -> candidata.reprovar(parecer);
            case EM_ANALISE -> candidata.colocarEmAnalise(parecer);
        }

        return repository.salvar(candidata);
    }
}
