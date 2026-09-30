package com.erica.missbrasil.infrastructure.adapter.out.persistence.repository;

import com.erica.missbrasil.domain.model.StatusCandidata;
import com.erica.missbrasil.infrastructure.adapter.out.persistence.entity.CandidataJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataCandidataRepository extends JpaRepository<CandidataJpaEntity, UUID> {

    Optional<CandidataJpaEntity> findByProtocolo(String protocolo);

    Optional<CandidataJpaEntity> findByEmailIgnoreCase(String email);

    long countByStatus(StatusCandidata status);

    @Query(value = """
        SELECT c FROM CandidataJpaEntity c
        WHERE (:status IS NULL OR c.status = :status)
          AND (:busca IS NULL OR LOWER(c.nomeCompleto) LIKE LOWER(CONCAT('%', :busca, '%'))
                             OR LOWER(c.cidade) LIKE LOWER(CONCAT('%', :busca, '%'))
                             OR LOWER(c.estado) LIKE LOWER(CONCAT('%', :busca, '%'))
                             OR LOWER(c.protocolo) LIKE LOWER(CONCAT('%', :busca, '%')))
        ORDER BY c.cadastradaEm DESC
    """)
    List<CandidataJpaEntity> pesquisar(
        @Param("status") StatusCandidata status,
        @Param("busca") String busca
    );
}
