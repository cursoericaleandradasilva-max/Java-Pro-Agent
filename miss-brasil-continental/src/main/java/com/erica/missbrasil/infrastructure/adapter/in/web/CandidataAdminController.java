package com.erica.missbrasil.infrastructure.adapter.in.web;

import com.erica.missbrasil.application.port.in.AvaliarCandidataUseCase;
import com.erica.missbrasil.application.port.in.ConsultarCandidatasUseCase;
import com.erica.missbrasil.application.usecase.ConsultarCandidatasService;
import com.erica.missbrasil.domain.model.Candidata;
import com.erica.missbrasil.domain.model.StatusCandidata;
import com.erica.missbrasil.infrastructure.adapter.in.web.dto.AvaliacaoRequest;
import com.erica.missbrasil.infrastructure.adapter.in.web.dto.CandidataAdminResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Map;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/candidatas")
public class CandidataAdminController {

    private final ConsultarCandidatasUseCase consultarUseCase;
    private final AvaliarCandidataUseCase avaliarUseCase;
    private final ConsultarCandidatasService metricasService;

    public CandidataAdminController(
        ConsultarCandidatasUseCase consultarUseCase,
        AvaliarCandidataUseCase avaliarUseCase,
        ConsultarCandidatasService metricasService
    ) {
        this.consultarUseCase = Objects.requireNonNull(consultarUseCase, "ConsultarCandidatasUseCase é obrigatório");
        this.avaliarUseCase = Objects.requireNonNull(avaliarUseCase, "AvaliarCandidataUseCase é obrigatório");
        this.metricasService = Objects.requireNonNull(metricasService, "ConsultarCandidatasService é obrigatório");
    }

    @GetMapping("/auth/check")
    public ResponseEntity<Map<String, Object>> verificarAutenticacao(Authentication authentication) {
        return ResponseEntity.ok(Map.of(
            "authenticated", true,
            "username", authentication.getName(),
            "roles", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        ));
    }

    @GetMapping
    public ResponseEntity<List<CandidataAdminResponse>> listar(
        @RequestParam(required = false) StatusCandidata status,
        @RequestParam(required = false) String busca
    ) {
        List<CandidataAdminResponse> candidatas = consultarUseCase.listarTodas(status, busca)
            .stream()
            .map(CandidataAdminResponse::aPartirDe)
            .toList();

        return ResponseEntity.ok(candidatas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidataAdminResponse> buscarPorId(@PathVariable UUID id) {
        return consultarUseCase.buscarPorId(id)
            .map(c -> ResponseEntity.ok(CandidataAdminResponse.aPartirDe(c)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/metricas")
    public ResponseEntity<ConsultarCandidatasService.ResumoContagem> obterMetricas() {
        return ResponseEntity.ok(metricasService.obterResumo());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CandidataAdminResponse> atualizarStatus(
        @PathVariable UUID id,
        @Valid @RequestBody AvaliacaoRequest request
    ) {
        Candidata avaliada = avaliarUseCase.executar(id, request.status(), request.parecer());
        return ResponseEntity.ok(CandidataAdminResponse.aPartirDe(avaliada));
    }
}
