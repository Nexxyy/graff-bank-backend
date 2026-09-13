package com.ledger.nexy.application.port.out.transaction

import com.ledger.nexy.domain.transaction.TransferIdempotency
import java.util.UUID

interface TransferIdempotencyGateway {
    
    fun findByKey(key: String): TransferIdempotency?
    
    fun reserve(idempotency: TransferIdempotency): Boolean
    
    fun complete(key: String, transferId: UUID)
    
}