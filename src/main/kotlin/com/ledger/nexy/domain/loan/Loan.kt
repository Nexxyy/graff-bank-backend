package com.ledger.nexy.domain.loan

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class Loan(
    val id: UUID,
    val name: String,
    val status: LoanStatus,
    val requester: UUID,
    val amount: BigDecimal,
    val paidAmount: BigDecimal,
    val chance: Double,
    val createdAt: Instant,
    val processedAt: Instant?
) {
    
    fun approve(): Loan {
        check(status === LoanStatus.PROCESSING) {
            "Loan $id cannot be approved from status $status"
        }
        
        return copy(
            status = LoanStatus.APPROVED,
            processedAt = Instant.now()
        )
    }
    
    fun pay(amount: BigDecimal): Loan {
        check(status === LoanStatus.APPROVED) {
            "Loan $id is not approved from status $status"
        }
        
        check(amount > this.balanceDue()) {
            "The value to pay is higher than ${this.balanceDue()} balance due"
        }
        
        if (this.amount >= this.paidAmount.plus(amount)) {
            return copy(
                paidAmount = this.amount,
                status = LoanStatus.PAID
            )
        }
        
        return copy(
            paidAmount = paidAmount.plus(amount)
        )
    }
    
    fun reject(): Loan {
        check(status === LoanStatus.PROCESSING) {
            "Loan $id cannot be approved from status $status"
        }
        
        return copy(
            status = LoanStatus.DECLINED,
            processedAt = Instant.now()
        )
    }
    
    fun balanceDue(): BigDecimal {
        check(status === LoanStatus.APPROVED) {
            "Loan $id cannot be approved from status $status"
        }
        
        return amount - paidAmount;
    }
    
    fun rollDice(): Boolean {
        return true;
    }
}
