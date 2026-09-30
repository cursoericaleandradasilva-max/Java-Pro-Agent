package com.erica.missbrasil.application.port.out;

import com.erica.missbrasil.domain.model.Candidata;
import com.erica.missbrasil.domain.model.StatusCandidata;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CandidataRepositoryPort {
    Candidata salvar(Candidata candidata);
    Optional<Candidata> buscarPorId(UUID id);
    Optional<Candidata> buscarPorProtocolo(String protocolo);
    Optional<Candidata> buscarPorEmail(String email);
    List<Candidata> listar(StatusCandidata filtroStatus, String termoBusca);
    long contarPorStatus(StatusCandidata status);
    long contarTotal();
}
