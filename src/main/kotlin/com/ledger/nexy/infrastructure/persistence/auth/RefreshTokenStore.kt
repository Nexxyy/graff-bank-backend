package com.ledger.nexy.infrastructure.persistence.auth

import com.ledger.nexy.application.port.out.auth.RefreshTokenGateway
import com.ledger.nexy.domain.auth.RefreshToken
import org.jdbi.v3.spring.JdbiRepository
import org.jdbi.v3.sqlobject.kotlin.BindKotlin
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@JdbiRepository
interface RefreshTokenStore : RefreshTokenGateway {
    
    @SqlUpdate(
        """
            INSERT INTO refresh_token (
                id,
                user_id,
                hash,
                expires_at,
                revoked
            )
            VALUES (
                :id,
                :userId,
                :hash,
                :expiresAt,
                :revoked
            )
        """
    )
    override fun save(@BindKotlin token: RefreshToken)
    
    
    @SqlUpdate(
        """
        DELETE FROM refresh_token
        WHERE hash = :hash
        """
    )
    override fun revoke(hash: String)
    
    @SqlQuery("SELECT * FROM refresh_token WHERE hash = :hash")
    override fun find(hash: String): RefreshToken?
    
}