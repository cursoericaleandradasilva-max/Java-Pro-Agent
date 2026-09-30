package com.erica.missbrasil.application.port.in;

import com.erica.missbrasil.domain.model.Candidata;
import com.erica.missbrasil.domain.model.StatusCandidata;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConsultarCandidatasUseCase {
    List<Candidata> listarTodas(StatusCandidata filtroStatus, String buscaNomeOuEstado);
    Optional<Candidata> buscarPorId(UUID id);
    Optional<Candidata> buscarPorProtocolo(String protocolo);
}
