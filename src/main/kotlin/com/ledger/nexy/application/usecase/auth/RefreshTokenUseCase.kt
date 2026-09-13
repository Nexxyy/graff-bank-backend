package com.ledger.nexy.application.usecase.auth

import com.ledger.nexy.api.dto.auth.RefreshResponse
import com.ledger.nexy.application.port.out.auth.TokensGateway
import com.ledger.nexy.application.port.out.auth.RefreshTokenGateway
import com.ledger.nexy.application.port.out.user.UserGateway
import com.ledger.nexy.application.usecase.auth.commands.RefreshCommand
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@Service
class RefreshTokenUseCase(
    private val refreshTokenGateway: RefreshTokenGateway,
    private val tokensGateway: TokensGateway,
    private val userGateway: UserGateway
) {
    
    @Transactional
    fun execute(command: RefreshCommand): RefreshResponse {
        val token = refreshTokenGateway.find(command.token) ?: throw ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "Invalid refresh token"
        )
        
        if (token.expiresAt.isBefore(Instant.now())) {
            refreshTokenGateway.revoke(command.token);
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Expired refresh token")
        }
        
        val user = userGateway.findById(token.userId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Invalid token owner"
        )
        
        val refreshToken = tokensGateway.createRefreshToken(user)
        val accessToken = tokensGateway.createAccessToken(user)
        
        refreshTokenGateway.revoke(command.token)
        refreshTokenGateway.save(refreshToken)
        
        return RefreshResponse(
            accessToken,
            refreshToken.hash
        )
    }
    
}