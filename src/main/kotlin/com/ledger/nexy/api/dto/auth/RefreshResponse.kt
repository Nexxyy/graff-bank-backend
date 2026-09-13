package com.ledger.nexy.api.dto.auth

data class RefreshResponse(
    val accessToken: String,
    val refreshToken: String,
)
