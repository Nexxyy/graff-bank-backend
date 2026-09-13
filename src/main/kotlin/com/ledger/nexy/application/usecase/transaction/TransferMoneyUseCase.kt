package com.ledger.nexy.application.usecase.transaction

import com.ledger.nexy.application.port.out.transaction.IdempotencyResolution
import com.ledger.nexy.application.port.out.user.AccountGateway
import com.ledger.nexy.application.port.out.transaction.TransferGateway
import com.ledger.nexy.application.port.out.transaction.TransferSigner
import com.ledger.nexy.application.usecase.transaction.commands.TransferMoneyCommand
import com.ledger.nexy.application.usecase.transaction.resolutions.TransferIdempotencyPolicy
import com.ledger.nexy.application.usecase.transaction.resolutions.TransferLockingPolicy
import com.ledger.nexy.domain.transaction.Transfer
import com.ledger.nexy.domain.transaction.TransferPayload
import com.ledger.nexy.domain.transaction.canonical
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import java.util.UUID

@Service
class TransferMoneyUseCase(
    private val accountGateway: AccountGateway,
    private val transferSigner: TransferSigner,
    private val transferGateway: TransferGateway,
    private val idempotencyPolicy: TransferIdempotencyPolicy,
    private val transferLockingPolicy: TransferLockingPolicy
) {
    
    @Transactional
    fun execute(command: TransferMoneyCommand): Transfer {
        when (val resolution = idempotencyPolicy.perform(command)) {
            IdempotencyResolution.Conflict -> throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "Idempotency state is inconsistent"
            )
            
            IdempotencyResolution.Processing -> throw ResponseStatusException(
                HttpStatus.PROCESSING,
                "Transfer still processing"
            )
            
            is IdempotencyResolution.Completed -> return transferGateway.find(resolution.transferId)
                ?: throw ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Idempotency state is inconsistent"
                )
            
            IdempotencyResolution.Reserved -> return executeTransfer(command)
        }
    }
    
    private fun executeTransfer(command: TransferMoneyCommand): Transfer {
        val lockedAccounts = transferLockingPolicy.perform(command)
        
        lockedAccounts.first.debit(command.amount)
        lockedAccounts.second.credit(command.amount)
        
        val payload = TransferPayload(
            UUID.randomUUID(), command.source,
            command.destination, command.amount,
            command.description, Instant.now()
        )
        
        val signature = transferSigner.signPayload(payload.canonical())
        
        val transfer = Transfer(
            payload.id, payload.source, payload.destination,
            payload.amount, payload.description, signature,
            payload.createdAt
        )
        
        accountGateway.bulkSave(
            lockedAccounts.first,
            lockedAccounts.second
        )
        
        transferGateway.create(transfer)
        idempotencyPolicy.complete(command.idempotencyKey, transfer.id)
        
        return transfer
    }
    
}