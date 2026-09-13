package com.ledger.nexy.application.usecase.loan

import com.ledger.nexy.application.port.out.loan.LoanGateway
import com.ledger.nexy.application.port.out.user.AccountGateway
import com.ledger.nexy.application.usecase.loan.command.PayLoanCommand
import com.ledger.nexy.domain.loan.Loan
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class PayLoanUseCase(
    private val loanGateway: LoanGateway,
    private val accountGateway: AccountGateway
) {
    
    @Transactional
    fun execute(command: PayLoanCommand): Loan {
        val loan = loanGateway.find(command.loanId)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Loan with id ${command.loanId} not found"
            )
        
        val account = accountGateway.findByOwnerId(command.owner)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Account with id ${command.owner} not found"
            )
        
        if (account.balance < loan.amount)
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Insufficient funds"
            )
        
        val paid = loan.pay(command.amount)
        
        accountGateway.save(account.debit(command.amount))
        loanGateway.save(paid)
        
        return paid;
    }
    
}