package com.ledger.nexy.application.usecase.loan.command

import java.math.BigDecimal
import java.util.UUID

data class RequestLoanCommand(
    val name: String,
    val requester: UUID,
    val amount: BigDecimal,
)
