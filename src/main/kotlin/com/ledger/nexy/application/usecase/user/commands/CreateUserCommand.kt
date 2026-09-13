package com.ledger.nexy.application.usecase.user.commands

data class CreateUserCommand(
    val name: String,
    val email: String,
    val document: String,
    val password: String
)