package com.ledger.nexy.application.usecase.account

import com.ledger.nexy.api.dto.account.AccountResponse
import com.ledger.nexy.application.port.out.user.AccountGateway
import com.ledger.nexy.application.usecase.account.command.FindAccountCommand
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class FindAccountUseCase(
    private val accountGateway: AccountGateway
) {
    
    fun execute(command: FindAccountCommand): AccountResponse {
        val account = (accountGateway.findByOwnerId(command.owner)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"))
        
        if (account.ownerId != command.authority) throw ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "Account does not match authority"
        )
        
        return AccountResponse(account.id, account.ownerId, account.balance);
    }
    
}