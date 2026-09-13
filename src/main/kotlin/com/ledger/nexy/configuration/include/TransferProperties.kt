package com.ledger.nexy.configuration.include

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.security.transactions")
data class TransferProperties(
    val privateKey: String,
    val publicKey: String,
)
