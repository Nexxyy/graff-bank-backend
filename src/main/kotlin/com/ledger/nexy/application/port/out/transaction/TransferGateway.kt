package com.ledger.nexy.application.port.out.transaction

import com.ledger.nexy.domain.transaction.Transfer
import java.util.UUID

interface TransferGateway {
    
    fun create(transaction: Transfer);
    
    fun find(id: UUID): Transfer?
    
}