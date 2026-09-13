package com.ledger.nexy.infrastructure.security

import com.ledger.nexy.configuration.include.JwtProperties
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.web.SecurityFilterChain
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties::class)
class SecurityConfiguration(private val properties: JwtProperties) {
    
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            cors { }
            csrf { disable() }
            httpBasic { disable() }
            formLogin { disable() }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
            oauth2ResourceServer { jwt { } }
            authorizeHttpRequests {
                authorize("/auth/login", permitAll)
                authorize("/auth/refresh", permitAll)
                authorize("/auth/logout", permitAll)
                authorize("/user", permitAll)
                authorize(anyRequest, authenticated)
            }
        }
        
        return http.build()
    }
    
    @Bean
    fun privateKey(): RSAPrivateKey {
        val bytes = decodePem(properties.privateKey, "PRIVATE KEY");
        
        return KeyFactory
            .getInstance("RSA")
            .generatePrivate(PKCS8EncodedKeySpec(bytes)) as RSAPrivateKey
    }
    
    @Bean
    fun publicKey(): RSAPublicKey {
        val bytes = decodePem(properties.publicKey, "PUBLIC KEY");
        
        return KeyFactory
            .getInstance("RSA")
            .generatePublic(X509EncodedKeySpec(bytes)) as RSAPublicKey
    }
    
    @Bean
    fun jwtEncoder(privateKey: RSAPrivateKey, publicKey: RSAPublicKey): JwtEncoder {
        val rsaKey = RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .build()
        
        return NimbusJwtEncoder(
            ImmutableJWKSet(
                JWKSet(rsaKey)
            )
        );
    }
    
    @Bean
    fun jwtDecoder(publicKey: RSAPublicKey): JwtDecoder {
        val decoder = NimbusJwtDecoder
            .withPublicKey(publicKey)
            .build()
        
        decoder.setJwtValidator(
            JwtValidators.createDefaultWithIssuer(
                properties.issuer
            )
        )
        
        return decoder
    }
    
    private fun decodePem(value: String, type: String): ByteArray {
        return value
            .replace("\\n", "")
            .replace("-----BEGIN $type-----", "")
            .replace("-----END $type-----", "")
            .replace("\\s".toRegex(), "")
            .let(Base64.getDecoder()::decode)
    }
}