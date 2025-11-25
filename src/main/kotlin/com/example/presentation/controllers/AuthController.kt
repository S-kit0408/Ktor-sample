package com.example.presentation.controllers

import com.example.application.usecases.auth.GetCurrentUserUseCase
import com.example.application.usecases.auth.SyncUserFromClerkUseCase
import com.example.common.utils.ErrorHandle
import com.example.presentation.mappers.UserMapper
import com.example.presentation.models.responses.ApiResponse
import com.example.presentation.models.responses.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.auth.principal
import io.ktor.server.response.*

class AuthController(
    private val syncUserFromClerkUseCase: SyncUserFromClerkUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) {

    // Clerk JWTからユーザーIDを取得するヘルパー
    private fun ApplicationCall.getClerkUserId(): String? {
        return principal<JWTPrincipal>()?.subject
    }

    // POST /api/auth/sync
    suspend fun syncUser(call: ApplicationCall) {
        try {
            val clerkUserId = call.getClerkUserId()
                ?: return call.respond(
                    HttpStatusCode.Unauthorized,
                    ErrorResponse(
                        error = "UNAUTHORIZED",
                        message = "Invalid or missing authentication token"
                    )
                )

            syncUserFromClerkUseCase.execute(clerkUserId)
                .onSuccess { userDto ->
                    val response = UserMapper.toResponse(userDto)
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse(
                            success = true,
                            data = response,
                            message = "User synced successfully"
                        )
                    )
                }
                .onFailure { exception ->
                    ErrorHandle(call, exception)
                }
        } catch (e: Exception) {
            ErrorHandle(call, e)
        }
    }

    // GET /api/auth/me
    suspend fun getCurrentUser(call: ApplicationCall) {
        try {
            val clerkUserId = call.getClerkUserId()
                ?: return call.respond(
                    HttpStatusCode.Unauthorized,
                    ErrorResponse(
                        error = "UNAUTHORIZED",
                        message = "Invalid or missing authentication token"
                    )
                )

            getCurrentUserUseCase.execute(clerkUserId)
                .onSuccess { userDto ->
                    val response = UserMapper.toResponse(userDto)
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse(
                            success = true,
                            data = response
                        )
                    )
                }
                .onFailure { exception ->
                    ErrorHandle(call, exception)
                }
        } catch (e: Exception) {
            ErrorHandle(call, e)
        }
    }
}