package com.erica.order.infrastructure.adapter.out.persistence.repository;

import com.erica.order.infrastructure.adapter.out.persistence.entity.IdempotencyKeyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataIdempotencyRepository extends JpaRepository<IdempotencyKeyJpaEntity, String> {
}
