package com.ledger.nexy.application.usecase.user

import com.ledger.nexy.application.port.out.user.UserGateway
import com.ledger.nexy.domain.user.User
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class IdentifyUserUseCase(private val userGateway: UserGateway) {
    
    fun execute(name: String): User {
        return userGateway.findById(UUID.fromString(name)) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "User with name $name not found"
        )
    }
}