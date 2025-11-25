package com.example.infrastructure.external

import com.example.domain.models.AuthProvider
import com.example.infrastructure.config.ClerkConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class ClerkApiClient {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private val baseUrl = "https://api.clerk.com/v1"
    private val secretKey = ClerkConfig.clerkSecretKey

    suspend fun getUserById(clerkUserId: String): ClerkUser? {
        return try {
            client.get("$baseUrl/users/$clerkUserId") {
                header("Authorization", "Bearer $secretKey")
            }.body<ClerkUser>()
        } catch (e: Exception) {
            println("Error fetching Clerk user: ${e.message}")
            null
        }
    }
}

@Serializable
data class ClerkUser(
    val id: String,
    val email_addresses: List<EmailAddress>,
    val first_name: String? = null,
    val last_name: String? = null,
    val image_url: String? = null,
    val external_accounts: List<ExternalAccount>? = null
) {
    val primaryEmail: String
        get() = email_addresses.firstOrNull()?.email_address ?: ""

    val fullName: String
        get() = listOfNotNull(first_name, last_name)
            .joinToString(" ")
            .ifBlank { primaryEmail.substringBefore("@") }

    val authProvider: AuthProvider
        get() {
            val hasGoogle = external_accounts?.any {
                it.provider == "google"
            } == true

            return if (hasGoogle) {
                AuthProvider.GOOGLE
            } else {
                AuthProvider.EMAIL
            }
        }
}

@Serializable
data class EmailAddress(
    val id: String,
    val email_address: String
)

@Serializable
data class ExternalAccount(
    val provider: String,
    val email_address: String? = null
)