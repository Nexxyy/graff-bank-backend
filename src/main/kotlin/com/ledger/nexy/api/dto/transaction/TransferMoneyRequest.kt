package com.ledger.nexy.api.dto.transaction

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.util.UUID

data class TransferMoneyRequest(
    
    @field:NotBlank
    @org.hibernate.validator.constraints.UUID
    val source: UUID,
    
    @field:NotBlank
    @org.hibernate.validator.constraints.UUID
    val destination: UUID,
    
    val description: String,
    
    @field:NotNull
    @field:DecimalMin("0.01")
    val amount: BigDecimal

)
