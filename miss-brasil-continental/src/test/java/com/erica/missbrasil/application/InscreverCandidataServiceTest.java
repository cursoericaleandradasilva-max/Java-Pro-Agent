package com.erica.missbrasil.application;

import com.erica.missbrasil.application.port.in.InscreverCandidataCommand;
import com.erica.missbrasil.application.port.out.CandidataRepositoryPort;
import com.erica.missbrasil.application.usecase.InscreverCandidataService;
import com.erica.missbrasil.domain.exception.RegraNegocioException;
import com.erica.missbrasil.domain.model.Candidata;
import com.erica.missbrasil.domain.model.StatusCandidata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InscreverCandidataServiceTest {

    @Mock
    private CandidataRepositoryPort repository;

    private InscreverCandidataService service;

    @BeforeEach
    void setUp() {
        service = new InscreverCandidataService(repository);
    }

    @Test
    @DisplayName("Deve realizar inscrição com sucesso e salvar no banco de dados")
    void deveInscreverCandidataComSucesso() {
        var command = new InscreverCandidataCommand(
            "Larissa Manoela Souza",
            "larissa@email.com",
            "(31) 99999-1234",
            "Av. Afonso Pena",
            "500",
            "Centro",
            "Belo Horizonte",
            "MG",
            "30130-001",
            21,
            1.74,
            55.0,
            List.of("data:image/png;base64,foto1")
        );

        when(repository.buscarPorEmail("larissa@email.com")).thenReturn(Optional.empty());
        when(repository.salvar(any(Candidata.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Candidata resultado = service.executar(command);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNomeCompleto()).isEqualTo("Larissa Manoela Souza");
        assertThat(resultado.getStatus()).isEqualTo(StatusCandidata.EM_ANALISE);
        assertThat(resultado.getProtocolo()).startsWith("MBC-");

        verify(repository).salvar(any(Candidata.class));
    }

    @Test
    @DisplayName("Deve rejeitar inscrição com e-mail já cadastrado (anti-duplicação)")
    void deveRejeitarEmailDuplicado() {
        var command = new InscreverCandidataCommand(
            "Larissa Manoela Souza",
            "duplicado@email.com",
            "(31) 99999-1234",
            "Av. Afonso Pena",
            "500",
            "Centro",
            "Belo Horizonte",
            "MG",
            "30130-001",
            21,
            1.74,
            55.0,
            List.of()
        );

        when(repository.buscarPorEmail("duplicado@email.com"))
            .thenReturn(Optional.of(Candidata.novaInscricao("Outra Candidata", new com.erica.missbrasil.domain.model.Contato("duplicado@email.com", "(11) 90000-0000"), new com.erica.missbrasil.domain.model.Endereco("Rua A", "1", "B", "C", "SP", "00000-000"), new com.erica.missbrasil.domain.model.Medidas(20, 1.70, 50.0), List.of())));

        assertThatThrownBy(() -> service.executar(command))
            .isInstanceOf(RegraNegocioException.class)
            .hasMessageContaining("Já existe uma inscrição cadastrada com o e-mail");
    }
}
