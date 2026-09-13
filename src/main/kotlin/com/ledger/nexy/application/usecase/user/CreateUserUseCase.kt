package com.ledger.nexy.application.usecase.user

import com.ledger.nexy.application.port.out.user.AccountGateway
import com.ledger.nexy.application.port.out.auth.PasswordGateway
import com.ledger.nexy.application.port.out.user.UserGateway
import com.ledger.nexy.application.usecase.user.commands.CreateUserCommand
import com.ledger.nexy.domain.account.Account
import com.ledger.nexy.domain.user.User
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Service
class CreateUserUseCase(
    private val userGateway: UserGateway,
    private val accountGateway: AccountGateway,
    private val passwordGateway: PasswordGateway
) {
    
    @Transactional
    fun execute(command: CreateUserCommand) {
        val userId = UUID.randomUUID();
        val existence = userGateway.findByEmailForUpdate(command.email)
        
        if (existence != null)
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "Account already exists"
            )
        
        val salty = passwordGateway.salty(command.password) ?: throw ResponseStatusException(
            HttpStatus.FORBIDDEN,
            "Password is required"
        )
        
        userGateway.create(
            User(
                userId, command.name, command.email,
                command.document, salty,
                100.0, 25, 7,
                Instant.now(),
            )
        )
        
        accountGateway.create(
            Account(
                UUID.randomUUID(), userId,
                BigDecimal.ZERO,
                Instant.now()
            )
        )
    }
}