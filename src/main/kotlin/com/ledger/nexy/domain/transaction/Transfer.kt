package com.ledger.nexy.domain.transaction

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class Transfer(
    val id: UUID,
    val source: UUID,
    val destination: UUID,
    val amount: BigDecimal,
    val description: String?,
    val signature: String,
    val createdAt: Instant
)
