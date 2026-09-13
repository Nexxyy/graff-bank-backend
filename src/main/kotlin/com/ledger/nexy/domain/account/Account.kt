package com.ledger.nexy.domain.account

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class Account(
    val id: UUID,
    val ownerId: UUID,
    val balance: BigDecimal,
    val createdAt: Instant,
) {
    
    fun credit(amount: BigDecimal): Account {
        require(amount > BigDecimal.ZERO) {
            "Deposit amount must be positive"
        }
        
        return copy(balance = balance + amount)
    }
    
    fun debit(amount: BigDecimal): Account {
        require(amount > BigDecimal.ZERO) {}
        
        require(balance >= amount) {
            throw IllegalArgumentException("Debit amount must be greater than zero.")
        }
        
        return copy(balance = balance - amount)
    }
    
}
