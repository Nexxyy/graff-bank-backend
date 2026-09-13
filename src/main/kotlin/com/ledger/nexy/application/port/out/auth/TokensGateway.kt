package com.ledger.nexy.application.port.out.auth

import com.ledger.nexy.domain.auth.RefreshToken
import com.ledger.nexy.domain.auth.TokenPayload
import com.ledger.nexy.domain.user.User

interface TokensGateway {
    
    fun createAccessToken(user: User): String
    
    fun createRefreshToken(user: User): RefreshToken
    
    fun checkAccessAccuracy(token: String): TokenPayload
    
}