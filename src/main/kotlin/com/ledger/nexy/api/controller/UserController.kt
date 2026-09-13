package com.ledger.nexy.api.controller

import com.ledger.nexy.api.dto.user.CreateUserRequest
import com.ledger.nexy.api.dto.user.IdentifyUserResponse
import com.ledger.nexy.api.dto.user.toIdentifyUserResponse
import com.ledger.nexy.application.usecase.user.commands.CreateUserCommand
import com.ledger.nexy.application.usecase.user.CreateUserUseCase
import com.ledger.nexy.application.usecase.user.IdentifyUserUseCase
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val createUserUseCase: CreateUserUseCase,
    private val identifyUserUseCase: IdentifyUserUseCase
) {
    
    @PostMapping
    fun createUser(@Valid @RequestBody request: CreateUserRequest) {
        val command = CreateUserCommand(
            name = request.name,
            email = request.email,
            document = request.document,
            password = request.password,
        )
        
        createUserUseCase.execute(command);
    }
    
    @GetMapping("/me")
    fun checkSession(authentication: Authentication): ResponseEntity<IdentifyUserResponse> {
        return ResponseEntity.ok(
            identifyUserUseCase
                .execute(authentication.name)
                .toIdentifyUserResponse()
        )
    }
    
}