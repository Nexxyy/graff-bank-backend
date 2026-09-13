package com.ledger.nexy.infrastructure.messaging

import com.ledger.nexy.application.port.out.outbox.OutboxGateway
import com.ledger.nexy.configuration.include.LoanRabbitProperties
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@EnableConfigurationProperties(LoanRabbitProperties::class)
@Component
class LoanOutboxPublisher(
    private val outboxGateway: OutboxGateway,
    private val rabbitTemplate: RabbitTemplate,
    private val properties: LoanRabbitProperties,
) {
    
    @Scheduled(fixedDelay = 1_000)
    fun publish() {
        val pending = outboxGateway.pickupClaim(100)
        
        pending.forEach { event ->
            rabbitTemplate.convertAndSend(
                properties.exchange,
                event.routingKey,
                event.payload
            ) { message ->
                message.messageProperties.contentType = "application/json"
                message
            }
            outboxGateway.markAsPublished(event.id!!)
        }
    }
    
}