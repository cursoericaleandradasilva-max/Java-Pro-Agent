package com.erica.missbrasil.domain;

import com.erica.missbrasil.domain.exception.RegraNegocioException;
import com.erica.missbrasil.domain.model.Candidata;
import com.erica.missbrasil.domain.model.Contato;
import com.erica.missbrasil.domain.model.Endereco;
import com.erica.missbrasil.domain.model.Medidas;
import com.erica.missbrasil.domain.model.StatusCandidata;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CandidataTest {

    @Test
    @DisplayName("Deve criar nova inscrição com status EM_ANALISE e protocolo válido")
    void deveCriarNovaInscricaoComSucesso() {
        Contato contato = new Contato("isabella@email.com", "(11) 98888-7777");
        Endereco endereco = new Endereco("Av Paulista", "1000", "Bela Vista", "São Paulo", "SP", "01310-100");
        Medidas medidas = new Medidas(22, 1.76, 56.0);
        List<String> fotos = List.of("data:image/jpeg;base64,sample1");

        Candidata candidata = Candidata.novaInscricao("Isabella Santos Silva", contato, endereco, medidas, fotos);

        assertThat(candidata).isNotNull();
        assertThat(candidata.getStatus()).isEqualTo(StatusCandidata.EM_ANALISE);
        assertThat(candidata.getProtocolo()).startsWith("MBC-");
        assertThat(candidata.getNomeCompleto()).isEqualTo("Isabella Santos Silva");
    }

    @Test
    @DisplayName("Deve validar idade mínima de 16 anos")
    void deveRejeitarIdadeInvalida() {
        assertThatThrownBy(() -> new Medidas(14, 1.70, 50.0))
            .isInstanceOf(RegraNegocioException.class)
            .hasMessageContaining("A idade da candidata deve ser entre 16 e 40 anos");
    }

    @Test
    @DisplayName("Deve permitir aprovação e reprovação de candidatas com parecer do jurado")
    void deveAlterarStatusDaCandidata() {
        Contato contato = new Contato("gabriela@email.com", "(21) 97777-6666");
        Endereco endereco = new Endereco("Rua Copacabana", "200", "Copacabana", "Rio de Janeiro", "RJ", "22020-001");
        Medidas medidas = new Medidas(24, 1.78, 59.0);

        Candidata candidata = Candidata.novaInscricao("Gabriela Oliveira", contato, endereco, medidas, List.of());

        candidata.aprovar("Excelente desenvoltura na passarela e perfil internacional.");
        assertThat(candidata.getStatus()).isEqualTo(StatusCandidata.APROVADA);
        assertThat(candidata.getParecerAvaliador()).contains("Excelente desenvoltura");

        candidata.reprovar("Não atendeu aos requisitos da fase final.");
        assertThat(candidata.getStatus()).isEqualTo(StatusCandidata.REPROVADA);
    }
}
