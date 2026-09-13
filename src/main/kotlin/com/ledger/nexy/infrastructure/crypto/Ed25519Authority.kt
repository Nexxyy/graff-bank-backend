package com.ledger.nexy.infrastructure.crypto

import com.ledger.nexy.configuration.include.TransferProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

@Component
@EnableConfigurationProperties(TransferProperties::class)
class Ed25519Authority(properties: TransferProperties) {
    
    private val privateKey: PrivateKey;
    private val publicKey: PublicKey;
    
    init {
        this.privateKey = this.convertPrivateKey(properties.privateKey);
        this.publicKey = convertPublicKey(properties.publicKey);
    }
    
    fun sign(value: String): String {
        val signature = Signature.getInstance("Ed25519")
        
        signature.initSign(privateKey)
        signature.update(value.toByteArray(Charsets.UTF_8));
        
        return Base64.getEncoder().encodeToString(signature.sign())
    }
    
    fun verify(value: String, signature: String): Boolean {
        val verifier = Signature.getInstance("Ed25519")
        
        verifier.initVerify(publicKey)
        verifier.update(value.toByteArray(Charsets.UTF_8));
        
        return verifier.verify(Base64.getDecoder().decode(signature))
    }
    
    private fun convertPrivateKey(value: String): PrivateKey {
        val bytes = decodePem(value, "PRIVATE KEY");
        
        return KeyFactory
            .getInstance("Ed25519")
            .generatePrivate(PKCS8EncodedKeySpec(bytes))
    }
    
    private fun convertPublicKey(value: String): PublicKey {
        val bytes = decodePem(value, "PUBLIC KEY");
        
        return KeyFactory
            .getInstance("Ed25519")
            .generatePublic(X509EncodedKeySpec(bytes))
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