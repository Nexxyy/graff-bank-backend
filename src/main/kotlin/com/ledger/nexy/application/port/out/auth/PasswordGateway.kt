package com.ledger.nexy.application.port.out.auth

interface PasswordGateway {

    fun salty(secret: String): String?
    
    fun matches(raw: String, encoded: String): Boolean
    
}