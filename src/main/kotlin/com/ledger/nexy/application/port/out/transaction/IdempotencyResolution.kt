package com.ledger.nexy.application.port.out.transaction

import java.util.UUID

sealed interface IdempotencyResolution {
    
    data object Processing : IdempotencyResolution
    
    data object Conflict : IdempotencyResolution
    
    data object Reserved: IdempotencyResolution
    
    data class Completed(val transferId: UUID) : IdempotencyResolution
    
}


