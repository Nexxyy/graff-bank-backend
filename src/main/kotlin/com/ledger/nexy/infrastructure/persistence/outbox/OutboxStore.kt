package com.ledger.nexy.infrastructure.persistence.outbox

import com.ledger.nexy.application.port.out.outbox.OutboxGateway
import com.ledger.nexy.domain.outbox.OutboxEvent
import org.jdbi.v3.spring.JdbiRepository
import org.jdbi.v3.sqlobject.kotlin.BindKotlin
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@JdbiRepository
interface OutboxStore : OutboxGateway {
    
    @SqlQuery(
        """
        UPDATE outbox_event
        SET status = 'PROCESSING'
        WHERE id IN (
            SELECT id
            FROM outbox_event
            WHERE status = 'PENDING'
            ORDER BY id
            FOR UPDATE SKIP LOCKED
            LIMIT :limit
        )
        RETURNING *;
        """
    )
    override fun pickupClaim(limit: Int): List<OutboxEvent>
    
    @SqlUpdate(
        """
        INSERT INTO outbox_event (
            aggregate_id, event_type,
            status, routing_key, payload,
            occurred_at
        ) VALUES (
            :aggregateId, :eventType,
            CAST(:status AS outbox_event_status),
            :routingKey, :payload, :occurredAt
        )
        """
    )
    override fun create(@BindKotlin event: OutboxEvent)
    
    @SqlUpdate(
        """
        UPDATE outbox_event SET
        status = 'PUBLISHED',
        published_at = CURRENT_TIMESTAMP
        WHERE id = :id
        """
    )
    override fun markAsPublished(id: Long)
    
}