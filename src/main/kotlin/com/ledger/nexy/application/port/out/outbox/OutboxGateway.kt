package com.ledger.nexy.application.port.out.outbox

import com.ledger.nexy.domain.outbox.OutboxEvent

interface OutboxGateway {
    
    fun create(event: OutboxEvent)
    
    fun pickupClaim(limit: Int): List<OutboxEvent>
    
    fun markAsPublished(id: Long)
    
}