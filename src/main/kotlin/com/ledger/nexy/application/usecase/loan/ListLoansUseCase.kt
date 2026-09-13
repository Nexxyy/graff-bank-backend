package com.ledger.nexy.application.usecase.loan

import com.ledger.nexy.application.port.out.loan.LoanGateway
import com.ledger.nexy.application.usecase.loan.command.ListLoansCommand
import com.ledger.nexy.domain.loan.Loan
import org.springframework.stereotype.Service

@Service
class ListLoansUseCase(private val loanGateway: LoanGateway) {
    
    fun execute(command: ListLoansCommand): List<Loan> {
        return loanGateway.listByOwner(command.owner);
    }
    
}