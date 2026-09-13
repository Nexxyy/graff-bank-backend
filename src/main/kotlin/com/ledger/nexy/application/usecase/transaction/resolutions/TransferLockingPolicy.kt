package com.ledger.nexy.application.usecase.transaction.resolutions

import com.ledger.nexy.application.port.out.user.AccountGateway
import com.ledger.nexy.application.usecase.transaction.commands.TransferMoneyCommand
import com.ledger.nexy.domain.account.Account
import org.springframework.stereotype.Component

@Component
class TransferLockingPolicy(
    private val accountGateway: AccountGateway
) {
    
    fun perform(command: TransferMoneyCommand): Pair<Account, Account> {
        val accounts = listOf(command.source, command.destination)
            .sorted()
            .map { id ->
                accountGateway.findForUpdate(id)
                    ?: throw IllegalArgumentException("Account $id not found")
            };
        
        if (accounts[0].id == command.source) return accounts[0] to accounts[1]
        
        return accounts[1] to accounts[0];
    }
    
}