package com.ledger.nexy.domain.user

import java.time.Instant
import java.util.UUID

open class User(
    val id: UUID,
    val name: String,
    val email: String,
    val document: String,
    val password: String,
    val creditLimit: Double,
    val closingDay: Int,
    val dueDay: Int,
    val createdAt: Instant,
)
