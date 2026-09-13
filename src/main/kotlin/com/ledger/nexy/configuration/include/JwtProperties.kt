package com.ledger.nexy.configuration.include

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.security.jwt")
data class JwtProperties(
    val privateKey: String,
    val publicKey: String,
    val issuer: String
)