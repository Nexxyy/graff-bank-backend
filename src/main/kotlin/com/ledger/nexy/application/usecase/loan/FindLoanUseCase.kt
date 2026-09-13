package com.ledger.nexy.application.usecase.loan

import com.ledger.nexy.application.port.out.loan.LoanGateway
import com.ledger.nexy.application.usecase.loan.command.FindLoanCommand
import com.ledger.nexy.domain.loan.Loan
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class FindLoanUseCase(
    private val loanGateway: LoanGateway
) {
    
    fun execute(command: FindLoanCommand): Loan {
        val loan = loanGateway.find(command.loan) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Loan with id ${command.loan} not found"
        )
        
        if (command.requester != loan.requester) {
            throw ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Unauthorized"
            )
        }
        
        return loan;
    }
}