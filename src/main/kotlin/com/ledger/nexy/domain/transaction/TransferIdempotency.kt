package com.ledger.nexy.domain.transaction

import java.time.Instant
import java.util.UUID

data class TransferIdempotency(
    val id: UUID,
    val key: String,
    val hash: String,
    val transferId: UUID?,
    val createdAt: Instant
)
