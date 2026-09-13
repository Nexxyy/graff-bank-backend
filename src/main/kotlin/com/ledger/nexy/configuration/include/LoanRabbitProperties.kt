package com.ledger.nexy.configuration.include

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.rabbitmq.loan-processing")
data class LoanRabbitProperties(
    val exchange: String,
    val queue: String,
    val routingKey: String,
    val dlq: String
)
