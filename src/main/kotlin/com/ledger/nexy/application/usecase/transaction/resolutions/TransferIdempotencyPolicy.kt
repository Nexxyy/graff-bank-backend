package com.ledger.nexy.application.usecase.transaction.resolutions

import com.ledger.nexy.application.port.out.transaction.IdempotencyResolution
import com.ledger.nexy.application.port.out.transaction.TransferIdempotencyGateway
import com.ledger.nexy.application.port.out.transaction.TransferSigner
import com.ledger.nexy.application.usecase.transaction.commands.TransferMoneyCommand
import com.ledger.nexy.application.usecase.transaction.commands.canonical
import com.ledger.nexy.domain.transaction.TransferIdempotency
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.UUID

@Component
class TransferIdempotencyPolicy(
    private val idempotencyGateway: TransferIdempotencyGateway,
    private val transferSigner: TransferSigner
) {
    
    fun perform(command: TransferMoneyCommand): IdempotencyResolution {
        val requestHash = transferSigner.hashPayload(command.canonical())
        val existing = idempotencyGateway.findByKey(command.idempotencyKey);
        
        if (existing != null) {
            if (existing.hash != requestHash)
                return IdempotencyResolution.Conflict;
            
            return existing.transferId
                ?.let { IdempotencyResolution.Completed(it) }
                ?: IdempotencyResolution.Processing
        }
        
        val reserved = idempotencyGateway.reserve(
            TransferIdempotency(
                id = UUID.randomUUID(),
                key = command.idempotencyKey,
                hash = requestHash,
                transferId = null,
                createdAt = Instant.now(),
            )
        )
        
        if (reserved) return IdempotencyResolution.Reserved;
        
        return IdempotencyResolution.Processing;
    }
    
    fun complete(key: String, transferId: UUID) {
        idempotencyGateway.complete(key, transferId);
    }
    
}