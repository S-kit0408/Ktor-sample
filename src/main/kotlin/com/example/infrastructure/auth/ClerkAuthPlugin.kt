package com.example.infrastructure.auth

import com.example.infrastructure.config.ClerkConfig
import com.example.presentation.models.responses.ErrorResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond
import kotlinx.coroutines.runBlocking

fun Application.configureClerkAuth() {
    install(Authentication) {
        jwt("clerk-jwt") {
            verifier(runBlocking { ClerkJWTVerifier.getVerifier() })

            validate { credential ->
                val clerkUserId = credential.payload.subject

                if (clerkUserId != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { _, _ ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    ErrorResponse(
                        error = "UNAUTHRIZED",
                        message = "Invalid or missing token"
                    )
                )
            }
        }
    }
}