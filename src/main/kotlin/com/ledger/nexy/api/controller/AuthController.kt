package com.ledger.nexy.api.controller

import com.ledger.nexy.api.dto.auth.LoginRequest
import com.ledger.nexy.api.dto.auth.LoginResponse
import com.ledger.nexy.api.dto.auth.RefreshResponse
import com.ledger.nexy.application.usecase.auth.LoginUseCase
import com.ledger.nexy.application.usecase.auth.RefreshTokenUseCase
import com.ledger.nexy.application.usecase.auth.commands.LoginCommand
import com.ledger.nexy.application.usecase.auth.commands.RefreshCommand
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val loginUseCase: LoginUseCase,
    private val refreshUseCase: RefreshTokenUseCase
) {
    
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.ok(
            loginUseCase.execute(
                LoginCommand(
                    request.document,
                    request.password
                )
            )
        )
    }
    
    @PostMapping("/refresh")
    fun refresh(@RequestHeader("Refresh-Token") refreshToken: String): ResponseEntity<RefreshResponse> {
        return ResponseEntity.ok(
            refreshUseCase.execute(RefreshCommand(refreshToken))
        )
    }
    
}