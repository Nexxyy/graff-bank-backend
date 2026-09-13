package com.ledger.nexy.domain.card

import java.time.Instant
import java.util.UUID

data class Card(
    val id: UUID,
    val owner: UUID,
    val name: String,
    val cardNumber: Char,
    val cvv: Char,
    val type: CardType,
    val status: CardStatus,
    val createdAt: Instant,
    val expiresAt: Instant,
)
