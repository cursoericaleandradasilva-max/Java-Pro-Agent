package com.erica.missbrasil.infrastructure.adapter.in.web;

import com.erica.missbrasil.application.port.in.ConsultarCandidatasUseCase;
import com.erica.missbrasil.application.port.in.InscreverCandidataCommand;
import com.erica.missbrasil.application.port.in.InscreverCandidataUseCase;
import com.erica.missbrasil.domain.model.Candidata;
import com.erica.missbrasil.infrastructure.adapter.in.web.dto.InscricaoRequest;
import com.erica.missbrasil.infrastructure.adapter.in.web.dto.InscricaoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/candidatas")
public class CandidataPublicController {

    private final InscreverCandidataUseCase inscreverCandidataUseCase;
    private final ConsultarCandidatasUseCase consultarCandidatasUseCase;

    public CandidataPublicController(
        InscreverCandidataUseCase inscreverCandidataUseCase,
        ConsultarCandidatasUseCase consultarCandidatasUseCase
    ) {
        this.inscreverCandidataUseCase = Objects.requireNonNull(inscreverCandidataUseCase, "InscreverCandidataUseCase é obrigatório");
        this.consultarCandidatasUseCase = Objects.requireNonNull(consultarCandidatasUseCase, "ConsultarCandidatasUseCase é obrigatório");
    }

    @PostMapping
    public ResponseEntity<InscricaoResponse> inscrever(@Valid @RequestBody InscricaoRequest request) {
        var command = new InscreverCandidataCommand(
            request.nomeCompleto(),
            request.email(),
            request.whatsapp(),
            request.logradouro(),
            request.numero(),
            request.bairro(),
            request.cidade(),
            request.estado(),
            request.cep(),
            request.idade(),
            request.alturaMetros(),
            request.pesoKg(),
            request.fotos()
        );

        Candidata inscrita = inscreverCandidataUseCase.executar(command);
        InscricaoResponse response = InscricaoResponse.aPartirDe(inscrita);

        URI location = URI.create("/api/v1/candidatas/protocolo/" + response.protocolo());
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(response);
    }

    @GetMapping("/protocolo/{protocolo}")
    public ResponseEntity<InscricaoResponse> consultarStatusPorProtocolo(@PathVariable String protocolo) {
        return consultarCandidatasUseCase.buscarPorProtocolo(protocolo)
            .map(c -> ResponseEntity.ok(InscricaoResponse.aPartirDe(c)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
