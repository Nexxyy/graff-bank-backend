package com.ledger.nexy.application.usecase.loan

import com.ledger.nexy.application.port.out.loan.LoanGateway
import com.ledger.nexy.application.port.out.user.AccountGateway
import com.ledger.nexy.application.usecase.loan.command.ProcessLoanCommand
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProcessLoanUseCase(
    private val loanGateway: LoanGateway,
    private val accountGateway: AccountGateway
) {
    
    @Transactional
    fun execute(command: ProcessLoanCommand) {
        val claimed = loanGateway.process(command.loanId)
        
        if (!claimed) return;
        
        val loan = loanGateway.find(command.loanId) ?: return
        val account = accountGateway.findByOwnerId(loan.requester) ?: return
        
        if (loan.rollDice()) {
            println("LOAN APPROVED")
            loanGateway.save(loan.approve());
            accountGateway.save(account.credit(loan.amount));
            return;
        }
        
        loanGateway.save(loan.reject());
    }
    
    
}