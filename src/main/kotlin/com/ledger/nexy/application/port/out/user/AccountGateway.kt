package com.ledger.nexy.application.port.out.user

import com.ledger.nexy.domain.account.Account
import java.util.UUID

interface AccountGateway {
    
    fun findById(id: UUID): Account?
    
    fun findByOwnerId(ownerId: UUID): Account?
    
    fun findForUpdate(id: UUID): Account?
    
    fun create(account: Account)
    
    fun save(account: Account)
    
    fun bulkSave(vararg accounts: Account)
    
}