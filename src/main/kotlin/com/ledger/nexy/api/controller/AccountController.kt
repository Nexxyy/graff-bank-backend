package com.ledger.nexy.api.controller

import com.ledger.nexy.api.dto.account.AccountResponse
import com.ledger.nexy.application.usecase.account.FindAccountUseCase
import com.ledger.nexy.application.usecase.account.command.FindAccountCommand
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/account")
class AccountController(private val findAccountUseCase: FindAccountUseCase) {
    
    @GetMapping("/{owner}")
    fun get(
        @PathVariable owner: UUID,
        authentication: Authentication
    ): ResponseEntity<AccountResponse> {
        return ResponseEntity.ok(
            findAccountUseCase.execute(
                FindAccountCommand(
                    owner,
                    UUID.fromString(authentication.name)
                )
            )
        )
    }
    
}