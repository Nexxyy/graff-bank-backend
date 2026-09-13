package com.ledger.nexy.application.usecase.auth

import com.ledger.nexy.api.dto.auth.LoginResponse
import com.ledger.nexy.application.port.out.auth.PasswordGateway
import com.ledger.nexy.application.port.out.auth.RefreshTokenGateway
import com.ledger.nexy.application.port.out.auth.TokensGateway
import com.ledger.nexy.application.port.out.user.UserGateway
import com.ledger.nexy.application.usecase.auth.commands.LoginCommand
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class LoginUseCase(
    private val userGateway: UserGateway,
    private val passwordGateway: PasswordGateway,
    private val refreshTokenGateway: RefreshTokenGateway,
    private val tokensGateway: TokensGateway
) {
    
    fun execute(command: LoginCommand): LoginResponse {
        val user = userGateway.findByDocument(command.document) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "User not found"
        )
        
        if (!passwordGateway.matches(
                command.password,
                user.password
            )
        ) throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong combination")
        
        val accessToken = tokensGateway.createAccessToken(user);
        val refreshToken = tokensGateway.createRefreshToken(user);
        
        refreshTokenGateway.save(refreshToken);
        
        return LoginResponse(accessToken, refreshToken.hash);
    }
    
}