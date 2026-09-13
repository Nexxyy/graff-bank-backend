package com.ledger.nexy.api.dto.user

import com.ledger.nexy.domain.user.User
import java.time.Instant
import java.util.UUID

data class IdentifyUserResponse(
    val id: UUID,
    val name: String,
    val email: String,
    val creditLimit: Double,
    val closingDay: Int,
    val dueDay: Int,
    val createdAt: Instant,
)

fun User.toIdentifyUserResponse(): IdentifyUserResponse = IdentifyUserResponse(
    id, name, email,
    creditLimit, closingDay,
    dueDay, createdAt
)
