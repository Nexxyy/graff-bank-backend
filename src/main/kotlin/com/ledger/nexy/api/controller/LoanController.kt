package com.ledger.nexy.api.controller

import com.ledger.nexy.api.dto.loan.InitLoanRequest
import com.ledger.nexy.api.dto.loan.PayLoanRequest
import com.ledger.nexy.application.usecase.loan.FindLoanUseCase
import com.ledger.nexy.application.usecase.loan.ListLoansUseCase
import com.ledger.nexy.application.usecase.loan.PayLoanUseCase
import com.ledger.nexy.application.usecase.loan.RequestLoanUseCase
import com.ledger.nexy.application.usecase.loan.command.FindLoanCommand
import com.ledger.nexy.application.usecase.loan.command.ListLoansCommand
import com.ledger.nexy.application.usecase.loan.command.PayLoanCommand
import com.ledger.nexy.application.usecase.loan.command.RequestLoanCommand
import com.ledger.nexy.domain.loan.Loan
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/loan")
class LoanController(
    private val requestLoanUseCase: RequestLoanUseCase,
    private val listLoansUseCase: ListLoansUseCase,
    private val payLoanUseCase: PayLoanUseCase,
    private val findLoanUseCase: FindLoanUseCase
) {
    
    @PostMapping
    fun requestLoan(
        @Valid @RequestBody request: InitLoanRequest,
        authentication: Authentication
    ): ResponseEntity<Loan> {
        return ResponseEntity.ok(
            requestLoanUseCase.execute(
                RequestLoanCommand(
                    name = request.name,
                    requester = UUID.fromString(authentication.name),
                    amount = request.amount
                )
            )
        )
    }
    
    @PostMapping("/pay")
    fun payAmount(
        @Valid @RequestBody request: PayLoanRequest,
        authentication: Authentication
    ): ResponseEntity<Loan> {
        return ResponseEntity.ok(
            payLoanUseCase.execute(
                PayLoanCommand(
                    loanId = request.loan,
                    owner = UUID.fromString(authentication.name),
                    amount = request.amount
                )
            )
        )
    }
    
    @GetMapping("/{loan}")
    fun findLoan(
        @PathVariable loan: UUID,
        authentication: Authentication
    ): ResponseEntity<Loan> {
        return ResponseEntity.ok(
            findLoanUseCase.execute(
                FindLoanCommand(
                    loan = loan,
                    requester = UUID.fromString(authentication.name),
                )
            )
        )
    }
    
    @GetMapping
    fun listLoans(authentication: Authentication): ResponseEntity<List<Loan>> {
        return ResponseEntity.ok(
            listLoansUseCase.execute(
                ListLoansCommand(UUID.fromString(authentication.name))
            )
        )
    }
    
}
