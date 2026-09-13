package com.ledger.nexy.application.usecase.account

import com.ledger.nexy.application.port.out.user.AccountGateway
import com.ledger.nexy.domain.account.Account
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class GetAccountUseCase(
    private val accountGateway: AccountGateway
) {
    
    fun execute(owner: UUID): Account {
        return accountGateway.findByOwnerId(owner)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found")
    }
    
}