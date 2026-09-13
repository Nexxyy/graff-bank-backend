package com.ledger.nexy.application.usecase.transaction.commands

import java.math.BigDecimal
import java.util.UUID
import kotlin.reflect.full.memberProperties

data class TransferMoneyCommand(
    val source: UUID,
    val destination: UUID,
    val description: String?,
    val amount: BigDecimal,
    val idempotencyKey: String,
) {
    
    init {
        require(source != destination) { "Source and destination must be different" }
        require(amount > BigDecimal.ZERO) { "Transfer amount must be greater than zero" }
        require(amount.scale() <= 2) { "Transfer amount cannot have more than 2 decimal places" }
        require(idempotencyKey.isNotBlank()) { "Idempotency-Key is required" }
    }
    
}

fun TransferMoneyCommand.canonical(): String {
    val fields = TransferMoneyCommand::class
        .memberProperties
        .map { field -> field.get(this) }
    
    return buildString {
        fields.forEach { append(it.toString()).append("|") }
    }
}
