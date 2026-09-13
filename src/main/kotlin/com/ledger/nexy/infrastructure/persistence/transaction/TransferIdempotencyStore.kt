package com.ledger.nexy.infrastructure.persistence.transaction

import com.ledger.nexy.application.port.out.transaction.TransferIdempotencyGateway
import com.ledger.nexy.domain.transaction.TransferIdempotency
import org.jdbi.v3.spring.JdbiRepository
import org.jdbi.v3.sqlobject.kotlin.BindKotlin
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.util.UUID

@JdbiRepository
interface TransferIdempotencyStore : TransferIdempotencyGateway {
    
    @SqlQuery("SELECT * FROM transfer_idempotency WHERE key = :key")
    override fun findByKey(key: String): TransferIdempotency?
    
    @SqlUpdate(
        """
        INSERT INTO transfer_idempotency (
            id,
            key,
            hash,
            transfer_id,
            created_at
        )
        VALUES (:id, :key, :hash, CAST(:transferId AS UUID), :createdAt)
        ON CONFLICT (key) DO NOTHING
        """
    )
    override fun reserve(@BindKotlin idempotency: TransferIdempotency): Boolean
    
    @SqlUpdate(
        """
        UPDATE transfer_idempotency
        SET transfer_id = :transferId
        WHERE key = :key
        """
    )
    override fun complete(key: String, transferId: UUID)
}