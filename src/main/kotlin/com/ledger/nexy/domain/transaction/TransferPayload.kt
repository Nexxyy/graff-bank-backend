package com.ledger.nexy.domain.transaction

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import kotlin.reflect.full.memberProperties

data class TransferPayload(
    val id: UUID,
    val source: UUID,
    val destination: UUID,
    val amount: BigDecimal,
    val description: String?,
    val createdAt: Instant,
)

fun TransferPayload.canonical(): String {
    val fields = TransferPayload::class
        .memberProperties
        .map { field -> field.get(this) }
    
    return buildString {
        fields.forEach { append(it.toString()).append("|") }
    }
}