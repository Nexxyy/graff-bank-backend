package com.ledger.nexy.api.dto.account

import com.ledger.nexy.domain.account.Account
import java.math.BigDecimal
import java.util.UUID

data class AccountResponse(
    val id: UUID,
    val ownerId: UUID,
    val balance: BigDecimal
)

fun Account.toResponse(): AccountResponse = AccountResponse(id, ownerId, balance)
