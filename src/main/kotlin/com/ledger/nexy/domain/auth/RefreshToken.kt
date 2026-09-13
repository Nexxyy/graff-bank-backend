package com.ledger.nexy.domain.auth

import java.time.Instant
import java.util.UUID

data class RefreshToken(
    val id: UUID,
    val userId: UUID,
    val hash: String,
    val expiresAt: Instant,
    val revoked: Boolean
)
