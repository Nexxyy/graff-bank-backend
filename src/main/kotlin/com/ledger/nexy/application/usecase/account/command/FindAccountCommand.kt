package com.ledger.nexy.application.usecase.account.command

import java.util.UUID

data class FindAccountCommand(
    val owner: UUID,
    val authority: UUID,
)
