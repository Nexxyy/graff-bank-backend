package com.ledger.nexy.infrastructure.crypto

import org.springframework.stereotype.Component
import java.security.MessageDigest

@Component
class Sha256Authority {
    
    fun hash(value: String): String {
        val digest = MessageDigest
            .getInstance("SHA-256")
            .digest(value.toByteArray(Charsets.UTF_8))
        
        return digest.joinToString("") {
            "%02x".format(it)
        }
    }
    
}