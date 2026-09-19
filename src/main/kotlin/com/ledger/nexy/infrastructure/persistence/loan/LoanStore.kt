package com.ledger.nexy.infrastructure.persistence.loan

import com.ledger.nexy.application.port.out.loan.LoanGateway
import com.ledger.nexy.domain.loan.Loan
import org.jdbi.v3.spring.JdbiRepository
import org.jdbi.v3.sqlobject.kotlin.BindKotlin
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.util.UUID

@JdbiRepository
interface LoanStore : LoanGateway {
    
    @SqlQuery("SELECT * FROM loan WHERE id = :id")
    override fun find(id: UUID): Loan?
    
    @SqlQuery("SELECT * FROM loan WHERE requester = :ownerId")
    override fun listByOwner(ownerId: UUID): List<Loan>
    
    @SqlUpdate(
        """
        INSERT INTO loan (
            id, name, status,
            requester, amount,
            paid_amount, chance,
            created_at, processed_at
        ) VALUES (
            :id, :name, CAST(:status AS loan_status),
            :requester, :amount,
            :paidAmount, :chance,
            :createdAt, :processedAt
        )
        """
    )
    override fun create(@BindKotlin loan: Loan)
    
    @SqlUpdate(
        """
        UPDATE loan
        SET name = :name,
        status = CAST(:status AS loan_status),
        requester = :requester,
        amount = :amount,
        paid_amount = :paidAmount,
        chance = :chance
        WHERE id = :id
        """
    )
    override fun save(@BindKotlin loan: Loan)
    
    @SqlUpdate("UPDATE loan SET status = 'PROCESSING' WHERE id = :id AND status = 'PENDING'")
    override fun process(id: UUID): Boolean
    
}