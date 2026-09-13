package com.ledger.nexy.api.dto.loan

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.util.UUID

data class InitLoanRequest(
    
    @field:NotBlank
    @field:Size(max = 64, min = 3)
    val name: String,
    
    @field:NotNull
    @field:DecimalMin("1")
    val amount: BigDecimal

)