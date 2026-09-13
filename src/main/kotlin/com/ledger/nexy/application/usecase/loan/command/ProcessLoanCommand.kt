package com.ledger.nexy.application.usecase.loan.command

import java.util.UUID

data class ProcessLoanCommand(
    val loanId: UUID
)
