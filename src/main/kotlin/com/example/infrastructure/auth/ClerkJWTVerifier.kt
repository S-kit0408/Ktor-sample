package com.example.infrastructure.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import com.example.infrastructure.config.ClerkConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.math.BigInteger
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.RSAPublicKeySpec
import java.util.Base64

object ClerkJWTVerifier {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private var cachedVerifier: JWTVerifier? = null

    suspend fun getVerifier(): JWTVerifier {
        if (cachedVerifier != null) {
            return cachedVerifier!!
        }

        val jwksUrl = "https://${ClerkConfig.clerkDomain}/.well-known/jwks.json"
        val jwks = client.get(jwksUrl).body<JWKSResponse>()

        val key = jwks.keys.first()
        val publicKey = buildRSAPublicKey(key.n, key.e)

        cachedVerifier = JWT.require(Algorithm.RSA256(publicKey, null))
            .withIssuer("https://${ClerkConfig.clerkDomain}")
            .build()

        return cachedVerifier!!
    }

    private fun buildRSAPublicKey(modulus: String, exponent: String): RSAPublicKey {
        val modulusBytes = Base64.getUrlDecoder().decode(modulus)
        val exponentBytes = Base64.getUrlDecoder().decode(exponent)

        val modulusBigInt = BigInteger(1, modulusBytes)
        val exponentBigInt = BigInteger(1, exponentBytes)

        val keySpec = RSAPublicKeySpec(modulusBigInt, exponentBigInt)
        val keyFactory = KeyFactory.getInstance("RSA")

        return keyFactory.generatePublic(keySpec) as RSAPublicKey
    }
}

@Serializable
data class JWKSResponse(
    val keys: List<JWK>
)

@Serializable
data class JWK(
    val kty: String,
    val use: String,
    val kid: String,
    val n: String,
    val e: String
)