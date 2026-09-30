package com.erica.missbrasil.application.port.in;

import com.erica.missbrasil.domain.model.Candidata;

public interface InscreverCandidataUseCase {
    Candidata executar(InscreverCandidataCommand command);
}
