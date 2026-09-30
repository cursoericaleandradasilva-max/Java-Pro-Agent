package com.erica.missbrasil.application.usecase;

import com.erica.missbrasil.application.port.in.InscreverCandidataCommand;
import com.erica.missbrasil.application.port.in.InscreverCandidataUseCase;
import com.erica.missbrasil.application.port.out.CandidataRepositoryPort;
import com.erica.missbrasil.domain.exception.RegraNegocioException;
import com.erica.missbrasil.domain.model.Candidata;
import com.erica.missbrasil.domain.model.Contato;
import com.erica.missbrasil.domain.model.Endereco;
import com.erica.missbrasil.domain.model.Medidas;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class InscreverCandidataService implements InscreverCandidataUseCase {

    private final CandidataRepositoryPort repository;

    public InscreverCandidataService(CandidataRepositoryPort repository) {
        this.repository = Objects.requireNonNull(repository, "CandidataRepositoryPort é obrigatório");
    }

    @Override
    @Transactional
    public Candidata executar(InscreverCandidataCommand command) {
        Objects.requireNonNull(command, "Command de inscrição não pode ser nulo");

        // 1. Cria e valida Value Objects
        Contato contato = new Contato(command.email(), command.whatsapp());
        Endereco endereco = new Endereco(
            command.logradouro(),
            command.numero(),
            command.bairro(),
            command.cidade(),
            command.estado(),
            command.cep()
        );
        Medidas medidas = new Medidas(command.idade(), command.alturaMetros(), command.pesoKg());

        // 2. Valida unicidade de e-mail (previne inscrições duplicadas)
        if (repository.buscarPorEmail(contato.email()).isPresent()) {
            throw new RegraNegocioException("Já existe uma inscrição cadastrada com o e-mail: " + contato.email());
        }

        // 3. Cria a nova entidade de modelo
        Candidata candidata = Candidata.novaInscricao(
            command.nomeCompleto(),
            contato,
            endereco,
            medidas,
            command.fotos()
        );

        // 4. Salva no banco de dados
        return repository.salvar(candidata);
    }
}
