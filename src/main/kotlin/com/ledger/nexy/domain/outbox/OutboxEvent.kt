package com.ledger.nexy.domain.outbox

import java.time.Instant
import java.util.UUID

data class OutboxEvent(
    val id: Long? = null,
    val aggregateId: UUID,
    val eventType: String,
    val status: OutboxStatus,
    val routingKey: String,
    val payload: String,
    val occurredAt: Instant,
    val publishedAt: Instant? = null,
)
