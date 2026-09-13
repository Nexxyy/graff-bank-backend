package com.ledger.nexy.application.port.out.auth

import com.ledger.nexy.domain.auth.RefreshToken

interface RefreshTokenGateway {
    
    fun save(token: RefreshToken);
    
    fun find(hash: String): RefreshToken?
    
    fun revoke(hash: String);
    
}