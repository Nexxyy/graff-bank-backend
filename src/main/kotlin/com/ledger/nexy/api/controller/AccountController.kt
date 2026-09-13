package com.ledger.nexy.api.controller

import com.ledger.nexy.api.dto.account.AccountResponse
import com.ledger.nexy.api.dto.account.toResponse
import com.ledger.nexy.application.usecase.account.GetAccountUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/account")
class AccountController(private val useCase: GetAccountUseCase) {
    
    @GetMapping("/{owner}")
    fun get(@PathVariable owner: UUID): ResponseEntity<AccountResponse> {
        val account = useCase.execute(owner);
        return ResponseEntity.ok(account.toResponse())
    }
    
}