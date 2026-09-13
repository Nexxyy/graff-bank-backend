package com.ledger.nexy.domain.loan.event

import java.util.UUID

data class LoanProcessEvent(
    val loanId: UUID,
)
