package com.ledger.nexy.infrastructure.security

import com.ledger.nexy.application.port.out.auth.TokensGateway
import com.ledger.nexy.configuration.include.JwtProperties
import com.ledger.nexy.domain.auth.RefreshToken
import com.ledger.nexy.domain.auth.TokenPayload
import com.ledger.nexy.domain.user.User
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Base64
import java.util.UUID

@Component
@EnableConfigurationProperties(JwtProperties::class)
class TokensProvider(
    private val encoder: JwtEncoder,
    private val decoder: JwtDecoder,
    private val properties: JwtProperties,
) : TokensGateway {
    
    override fun createAccessToken(user: User): String {
        val now = Instant.now()
        
        val claims = JwtClaimsSet.builder()
            .issuer(properties.issuer)
            .subject(user.id.toString())
            .issuedAt(now)
            .expiresAt(now.plus(30, ChronoUnit.DAYS))
            .claim("type", "access")
            .build()
        
        return encoder.encode(JwtEncoderParameters.from(claims)).tokenValue;
    }
    
    override fun createRefreshToken(user: User): RefreshToken { // Refactor this to @Sha256Authority
        val byteArray = ByteArray(32) // 256 bits
        
        SecureRandom().nextBytes(byteArray);
        
        val token = Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(
                MessageDigest.getInstance("SHA-256")
                    .digest(byteArray)
            )
        return RefreshToken(
            UUID.randomUUID(), user.id, token,
            Instant.now().plus(30, ChronoUnit.DAYS), false
        )
    }
    
    override fun checkAccessAccuracy(token: String): TokenPayload {
        val jwt = decoder.decode(token)
        
        require(jwt.getClaimAsString("type") == "access")
        
        return TokenPayload(
            UUID.fromString(jwt.subject)
        )
    }
    
}