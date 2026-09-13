package com.ledger.nexy.application.usecase.loan.command

import java.math.BigDecimal
import java.util.UUID

data class PayLoanCommand(
    val loanId: UUID,
    val owner: UUID,
    val amount: BigDecimal,
)
