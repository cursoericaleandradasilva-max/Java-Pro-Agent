package com.erica.missbrasil.application.port.in;

import com.erica.missbrasil.domain.model.Candidata;
import com.erica.missbrasil.domain.model.StatusCandidata;

import java.util.UUID;

public interface AvaliarCandidataUseCase {
    Candidata executar(UUID candidataId, StatusCandidata novoStatus, String parecer);
}
