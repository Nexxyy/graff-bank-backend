package com.ledger.nexy.api.dto.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateUserRequest(

    @field:NotBlank
    @field:Size(min = 3, max = 50)
    val name: String,

    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    val document: String,

    @field:NotBlank
    val password: String

)
