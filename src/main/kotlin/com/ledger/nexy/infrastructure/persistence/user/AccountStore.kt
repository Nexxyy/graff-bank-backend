package com.ledger.nexy.infrastructure.persistence.user

import com.ledger.nexy.domain.account.Account
import com.ledger.nexy.application.port.out.user.AccountGateway
import org.jdbi.v3.spring.JdbiRepository
import org.jdbi.v3.sqlobject.kotlin.BindKotlin
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.util.UUID

@JdbiRepository
interface AccountStore : AccountGateway {
    
    @SqlQuery("SELECT * FROM account WHERE account.id = :id")
    override fun findById(id: UUID): Account?
    
    @SqlQuery("SELECT * FROM account WHERE account.owner_id = :ownerId")
    override fun findByOwnerId(ownerId: UUID): Account?
    
    @SqlQuery("SELECT * FROM account WHERE account.id = :id FOR UPDATE")
    override fun findForUpdate(id: UUID): Account?
    
    @SqlUpdate(
        """
            INSERT INTO account (
                id,
                owner_id,
                balance
            )
            VALUES (
                :id,
                :ownerId,
                :balance
            )
        """
    )
    override fun create(@BindKotlin account: Account)
    
    @SqlUpdate(
        """
        UPDATE account
        SET balance = :balance
        WHERE id = :id
        """
    )
    override fun save(@BindKotlin account: Account)
    
    @SqlBatch(
        """
        UPDATE account
        SET balance = :balance
        WHERE id = :id
        """
    )
    override fun bulkSave(@BindKotlin vararg accounts: Account)
    
}
