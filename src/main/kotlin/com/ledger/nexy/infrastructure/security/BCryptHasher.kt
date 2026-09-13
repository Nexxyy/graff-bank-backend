package com.ledger.nexy.infrastructure.security

import com.ledger.nexy.application.port.out.auth.PasswordGateway
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class BCryptHasher: PasswordGateway {
    
    private val encoder = BCryptPasswordEncoder();
    
    override fun salty(secret: String): String? = encoder.encode(secret);
    
    override fun matches(raw: String, encoded: String): Boolean = encoder.matches(raw, encoded);
    
}