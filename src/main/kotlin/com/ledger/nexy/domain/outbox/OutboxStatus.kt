package com.ledger.nexy.domain.outbox

enum class OutboxStatus {
    PENDING,
    PUBLISHED,
    PROCESSING,
}