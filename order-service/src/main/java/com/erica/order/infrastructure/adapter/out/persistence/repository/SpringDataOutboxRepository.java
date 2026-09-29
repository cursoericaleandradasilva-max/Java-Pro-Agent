package com.erica.order.infrastructure.adapter.out.persistence.repository;

import com.erica.order.infrastructure.adapter.out.persistence.entity.OutboxJpaEntity;
import com.erica.order.infrastructure.outbox.OutboxEventRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataOutboxRepository extends JpaRepository<OutboxJpaEntity, UUID> {

    @Query(value = """
        SELECT o FROM OutboxJpaEntity o
        WHERE o.status = :status
        ORDER BY o.createdAt ASC
    """)
    List<OutboxJpaEntity> findByStatusOrderByCreatedAtAsc(
        @Param("status") OutboxEventRecord.OutboxStatus status,
        Pageable pageable
    );
}
