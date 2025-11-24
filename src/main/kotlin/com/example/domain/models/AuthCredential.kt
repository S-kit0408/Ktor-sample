package com.example.domain.models

import kotlinx.datetime.Instant
import com.github.f4b6a3.ulid.UlidCreator

data class AuthCredential(
    val id: AuthCredentialId,
    val userId: UserId,
    val authType: AuthType,
    val passwordHash: String? = null,
    val oauthProvider: String? = null,
    val oauthProviderId: String? = null,
    val createdAt: Instant
) {
    init {
        when (authType) {
            AuthType.PASSWORD -> {
                require(passwordHash != null) { "Password hash is required for PASSWORD auth type" }
                require(oauthProvider == null && oauthProviderId == null) {
                    "OAuth fields must be null for PASSWORD auth type"
                }
            }
            AuthType.GOOGLE -> {
                require(oauthProvider != null && oauthProviderId != null) {
                    "OAuth provider and provider ID are required for GOOGLE auth type"
                }
                require(passwordHash == null) {
                    "Password hash must be null for GOOGLE auth type"
                }
            }
        }
    }

    companion object {
        fun createPasswordAuth(
            userId: UserId,
            passwordHash: String,
            now: Instant
        ): AuthCredential {
            return AuthCredential(
                id = AuthCredentialId.generate(),
                userId = userId,
                authType = AuthType.PASSWORD,
                passwordHash = passwordHash,
                createdAt = now
            )
        }

        fun createGoogleAuth(
            userId: UserId,
            googleId: String,
            now: Instant
        ): AuthCredential {
            return AuthCredential(
                id = AuthCredentialId.generate(),
                userId = userId,
                authType = AuthType.GOOGLE,
                oauthProvider = "google",
                oauthProviderId = googleId,
                createdAt = now
            )
        }
    }
}


enum class AuthType {
    PASSWORD,
    GOOGLE;

    companion object {
        fun fromString(value: String): AuthType {
            return valueOf(value.uppercase())
        }
    }
}

@JvmInline
value class AuthCredentialId(val value: String) {
    init {
        require(isValid(value)) { "Invalid ULID format: $value" }
    }

    companion object {
        fun generate(): AuthCredentialId {
            return AuthCredentialId(UlidCreator.getUlid().toString())
        }

        fun of(value: String): AuthCredentialId {
            require(value.isNotBlank()) { "AuthCredentialId cannot be blank" }
            return AuthCredentialId(value)
        }

        private fun isValid(value: String): Boolean {
            return value.length == 26 && value.all { it in '0'..'9' || it in 'A'..'Z' }
        }
    }
}