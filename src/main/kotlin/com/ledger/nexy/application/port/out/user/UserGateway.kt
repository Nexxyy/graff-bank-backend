package com.ledger.nexy.application.port.out.user

import com.ledger.nexy.domain.user.User
import java.util.UUID

interface UserGateway {

    fun create(user: User)
    
    fun findByEmailForUpdate(email: String): User?
    
    fun findByDocument(document: String): User?
    
    fun findById(id: UUID): User?

}