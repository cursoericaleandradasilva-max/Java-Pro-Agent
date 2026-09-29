package com.erica.order.infrastructure.adapter.out.persistence.repository;

import com.erica.order.infrastructure.adapter.out.persistence.entity.PedidoJpaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataPedidoRepository extends JpaRepository<PedidoJpaEntity, UUID> {

    @Override
    @EntityGraph(attributePaths = {"itens"})
    Optional<PedidoJpaEntity> findById(UUID id);
}
