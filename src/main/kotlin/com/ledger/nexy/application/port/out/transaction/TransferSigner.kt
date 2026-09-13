package com.ledger.nexy.application.port.out.transaction

interface TransferSigner {
    
    fun hashPayload(payload: String): String;
    
    fun signPayload(payload: String): String;
    
}