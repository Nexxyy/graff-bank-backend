package com.ledger.nexy.infrastructure.messaging

import com.ledger.nexy.application.usecase.loan.ProcessLoanUseCase
import com.ledger.nexy.application.usecase.loan.command.ProcessLoanCommand
import com.ledger.nexy.domain.loan.event.LoanProcessEvent
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

@Component
class LoanProcessingConsumer(
    private val processLoanUseCase: ProcessLoanUseCase,
    private val mapper: ObjectMapper
) {
    
    @RabbitListener(queues = ["loan.processing"])
    fun consume(payload: String) {
        Thread.sleep(120_000)
        val message = mapper.readValue(payload, LoanProcessEvent::class.java)

        processLoanUseCase.execute(
            ProcessLoanCommand(message.loanId)
        )
    }
    
}