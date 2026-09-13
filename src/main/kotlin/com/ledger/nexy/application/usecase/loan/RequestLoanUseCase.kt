package com.ledger.nexy.application.usecase.loan

import com.ledger.nexy.application.port.out.loan.LoanGateway
import com.ledger.nexy.application.port.out.outbox.OutboxGateway
import com.ledger.nexy.application.usecase.loan.command.RequestLoanCommand
import com.ledger.nexy.domain.loan.Loan
import com.ledger.nexy.domain.loan.LoanStatus
import com.ledger.nexy.domain.loan.event.LoanProcessEvent
import com.ledger.nexy.domain.outbox.OutboxEvent
import com.ledger.nexy.domain.outbox.OutboxStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.ObjectMapper
import java.math.BigDecimal
import java.time.Instant
import java.util.Random
import java.util.UUID

@Service
class RequestLoanUseCase(
    private val mapper: ObjectMapper,
    private val loanGateway: LoanGateway,
    private val outboxGateway: OutboxGateway
) {
    
    @Transactional
    fun execute(command: RequestLoanCommand): Loan {
        val loan = Loan(
            id = UUID.randomUUID(),
            name = command.name,
            status = LoanStatus.PENDING,
            requester =command.requester,
            amount = command.amount,
            paidAmount = BigDecimal.ZERO,
            chance = Random().nextDouble(),
            createdAt = Instant.now(),
            processedAt = null
        );
        
        this.loanGateway.create(loan);
        
        val event = LoanProcessEvent(loanId = loan.id)
        
        val outbox = OutboxEvent(
            aggregateId = loan.id,
            eventType = "LoanProcessingRequested",
            routingKey = "loan.processing",
            status = OutboxStatus.PENDING,
            payload = mapper.writeValueAsString(event),
            occurredAt = Instant.now()
        )
        
        this.outboxGateway.create(outbox)
        
        return loan
    }
    
}