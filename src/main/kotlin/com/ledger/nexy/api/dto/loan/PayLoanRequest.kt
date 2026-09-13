package com.ledger.nexy.api.dto.loan

import jakarta.validation.constraints.DecimalMin
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal
import java.util.UUID

data class PayLoanRequest(
    
    val loan: UUID,
    
    @field:NotNull
    @field:DecimalMin("1")
    val amount: BigDecimal
    
)
