package com.ledger.nexy.application.usecase.loan.command

import java.util.UUID

data class FindLoanCommand(
    val loan: UUID,
    val requester: UUID,
)
