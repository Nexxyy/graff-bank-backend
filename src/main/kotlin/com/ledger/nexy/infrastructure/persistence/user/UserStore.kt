package com.ledger.nexy.infrastructure.persistence.user

import com.ledger.nexy.application.port.out.user.UserGateway
import com.ledger.nexy.domain.user.User
import org.jdbi.v3.spring.JdbiRepository
import org.jdbi.v3.sqlobject.kotlin.BindKotlin
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.util.UUID

@JdbiRepository
interface UserStore : UserGateway {
    
    @SqlUpdate(
        """
        INSERT INTO users (
            id,
            name,
            email,
            document,
            password,
            credit_limit,
            closing_day,
            due_day
        )
        VALUES (
            :id,
            :name,
            :email,
            :document,
            :password,
            :creditLimit,
            :closingDay,
            :dueDay
        )
        """
    )
    override fun create(@BindKotlin user: User)
    
    @SqlQuery("SELECT * FROM users WHERE email = :email FOR UPDATE")
    override fun findByEmailForUpdate(email: String): User?
    
    @SqlQuery("SELECT * FROM users WHERE document = :document")
    override fun findByDocument(document: String): User?
    
    @SqlQuery("SELECT * FROM users WHERE id = :id")
    override fun findById(id: UUID): User?
    
}