package com.ledger.nexy.infrastructure.security

import com.ledger.nexy.application.port.out.transaction.TransferSigner
import com.ledger.nexy.infrastructure.crypto.Ed25519Authority
import com.ledger.nexy.infrastructure.crypto.Sha256Authority
import org.springframework.stereotype.Component

@Component
class TransferSignerProvider(
    private val edAuthority: Ed25519Authority,
    private val shaAuthority: Sha256Authority
) : TransferSigner {
    
    override fun hashPayload(payload: String): String {
        return shaAuthority.hash(payload)
    }
    
    override fun signPayload(payload: String): String {
        return edAuthority.sign(payload)
    }
    
}