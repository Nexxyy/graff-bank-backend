package com.ledger.nexy.infrastructure.persistence.transaction

import com.ledger.nexy.application.port.out.transaction.TransferGateway
import com.ledger.nexy.domain.transaction.Transfer
import org.jdbi.v3.spring.JdbiRepository
import org.jdbi.v3.sqlobject.kotlin.BindKotlin
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.util.UUID

@JdbiRepository
interface TransfersStore : TransferGateway {
    
    @SqlQuery("""
        SELECT
        id, source, destination, amount,
        description, signature, created_at
        FROM transfers WHERE id = :id
        """
    )
    override fun find(id: UUID): Transfer?
    
    @SqlUpdate(
        """
        INSERT INTO transfers (
            id,
            source,
            destination,
            amount,
            description,
            signature,
            created_at
        ) VALUES (
            :id, :source, :destination, :amount, :description,
            :signature, :createdAt
        )
        """
    )
    override fun create(@BindKotlin transaction: Transfer);
    
}