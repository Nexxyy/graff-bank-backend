package com.ledger.nexy.api.controller

import com.ledger.nexy.api.dto.transaction.TransferMoneyRequest
import com.ledger.nexy.application.usecase.transaction.TransferMoneyUseCase
import com.ledger.nexy.application.usecase.transaction.commands.TransferMoneyCommand
import com.ledger.nexy.domain.transaction.Transfer
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/transfer")
class TransactionsController(
    private val transferMoneyUseCase: TransferMoneyUseCase
) {
    
    @PostMapping
    fun transferMoney(
        @RequestHeader("Idempotency-Key") idempotencyKey: String,
        @RequestBody request: TransferMoneyRequest
    ): ResponseEntity<Transfer> {
        val transfer = transferMoneyUseCase.execute(
            TransferMoneyCommand(
                request.source, request.destination,
                request.description,
                request.amount, idempotencyKey
            )
        )
        
        return ResponseEntity.ok(transfer)
    }
    
    
}