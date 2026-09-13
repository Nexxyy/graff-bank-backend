package com.ledger.nexy.application.usecase.auth.commands

data class LoginCommand(
    val document: String,
    val password: String,
)