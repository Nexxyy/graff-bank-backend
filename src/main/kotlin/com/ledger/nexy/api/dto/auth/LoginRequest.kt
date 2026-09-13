package com.ledger.nexy.api.dto.auth

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginRequest(

    @field:NotBlank
    @field:Size(max = 11, min = 11)
    val document: String,

    @field:NotBlank
    @field:Size(min = 8)
    val password: String

)