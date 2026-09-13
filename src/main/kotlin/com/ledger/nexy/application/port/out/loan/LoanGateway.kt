package com.ledger.nexy.application.port.out.loan

import com.ledger.nexy.domain.loan.Loan
import java.util.UUID

interface LoanGateway {
    
    fun find(id: UUID): Loan?
    
    fun listByOwner(ownerId: UUID): List<Loan>
    
    fun save(loan: Loan)
    
    fun create(loan: Loan)
    
    fun process(id: UUID): Boolean
    
}