package com.ledger.nexy.api.dto.auth

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
)
