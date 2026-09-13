package com.ledger.nexy.configuration

import com.ledger.nexy.configuration.include.LoanRabbitProperties
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(LoanRabbitProperties::class)
@Configuration
class RabbitConfiguration(
    private val properties: LoanRabbitProperties
) {
    
    @Bean
    fun loanProcessingExchange(): DirectExchange =
        DirectExchange(properties.exchange)
    
    @Bean
    fun loanProcessingQueue(): Queue =
        QueueBuilder
            .durable(properties.queue)
            .deadLetterExchange("")
            .deadLetterRoutingKey(properties.dlq)
            .build()
    
    @Bean
    fun loanProcessingDlq(): Queue =
        QueueBuilder
            .durable(properties.dlq)
            .build()
    
    @Bean
    fun loanProcessingBinding(
        @Qualifier("loanProcessingQueue") queue: Queue,
        @Qualifier("loanProcessingExchange") exchange: DirectExchange
    ): Binding =
        BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(properties.routingKey)
}
