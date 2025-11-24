package com.example.presentation.controllers

import com.example.application.dto.ChangePasswordDto
import com.example.application.usecases.auth.LoginWithGoogleUseCase
import com.example.application.usecases.auth.LoginWithPasswordUseCase
import com.example.application.usecases.auth.RegisterUserWithGoogleUseCase
import com.example.application.usecases.auth.DeleteAuthCredentialUseCase
import com.example.application.usecases.user.RegisterUserWithPasswordUseCase
import com.example.presentation.mappers.AuthMapper
import com.example.presentation.models.requests.*
import com.example.presentation.models.responses.ApiResponse
import com.example.presentation.models.responses.ErrorResponse
import com.example.common.utils.ErrorHandle
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

class AuthController(
    private val registerUserWithPasswordUseCase: RegisterUserWithPasswordUseCase,
    private val registerUserWithGoogleUseCase: RegisterUserWithGoogleUseCase,
    private val loginWithPasswordUseCase: LoginWithPasswordUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val deleteAuthCredentialUseCase: DeleteAuthCredentialUseCase,
) {
    // Post api/auth/register/password
    suspend fun registerWithPassword(call: ApplicationCall) {
        try {
            val request = call.receive<RegisterUserRequest>()
            val dto = AuthMapper.toRegisterPasswordDto(request)

            registerUserWithPasswordUseCase.execute(dto)
                .onSuccess { result ->
                    val response = AuthMapper.toUserWithAuthResponse(result)
                    call.respond(
                        HttpStatusCode.Created,
                        ApiResponse(
                            success = true,
                            data = response,
                            message = "User registered successfully!"
                        )
                    )
                }
                .onFailure { exception ->
                    ErrorHandle(call, exception)
                }

        } catch (e:Exception) {
            ErrorHandle(call, e)
        }
    }

    // Post /api/auth/register/google
    suspend fun registerWithGoogle(call: ApplicationCall) {
        try {
            val request = call.receive<RegisterUserRequest>()
            val dto = AuthMapper.toRegisterGoogleDto(request)

            registerUserWithGoogleUseCase.execute(dto)
                .onSuccess { result ->
                    val response = AuthMapper.toUserWithAuthResponse(result)
                    call.respond(
                        HttpStatusCode.Created,
                        ApiResponse(
                            success = true,
                            data = response,
                            message = "User registered successfully!"
                        )
                    )
                }
                .onFailure { exception ->
                    ErrorHandle(call, exception)
                }
        } catch (e:Exception) {
            ErrorHandle(call, e)
        }
    }

    // POSt /api/auth/login/password
    suspend fun loginWithPassword(call: ApplicationCall) {
        try {
            val request = call.receive<LoginRequest>()
            val dto = AuthMapper.toLoginPasswordDto(request)

            loginWithPasswordUseCase.execute(dto)
                .onSuccess { result ->
                    val response = AuthMapper.toAuthResponse(result)
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse(
                            success = true,
                            data = response,
                            message = "User logged in!"
                        )
                    )
                }
                .onFailure { exception ->
                    ErrorHandle(call, exception)
                }
        } catch (e:Exception) {
            ErrorHandle(call, e)
        }
    }

    // POST api/auth/login/gooogle
    suspend fun loginWithGoogle(call: ApplicationCall) {
        try {
            val request = call.receive<LoginRequest>()
            val dto = AuthMapper.toLoginGoogleDto(request)

            loginWithGoogleUseCase.execute(dto)
                .onSuccess { result ->
                    val response = AuthMapper.toAuthResponse(result)
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse(
                            success = true,
                            data = response,
                            message = "User logged in!"
                        )
                    )
                }
                .onFailure { exception ->
                    ErrorHandle(call, exception)
                }
        } catch (e:Exception) {
            ErrorHandle(call, e)
        }
    }

    // DELETE /api/auth/credentials/{credentialId}
    suspend fun deleteAuthCredential(call: ApplicationCall) {
        try {
            val credentialId = call.parameters["credentialId"]
                ?: return call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse(
                        error = "BAD_REQUEST",
                        message = "User ID is required"
                    )
                )

            val userId = call.parameters["userId"]
                ?: return call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse(
                        error = "BAD_REQUEST",
                        message = "User ID is required"
                    )
                )

            deleteAuthCredentialUseCase.execute(credentialId, userId)
                .onSuccess { result ->
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse<Unit>(
                            success = true,
                            message = "Authentication method deleted successfully"
                        )
                    )
                }
                .onFailure { exception ->
                    ErrorHandle(call, exception)
                }
        } catch (e:Exception) {
            ErrorHandle(call, e)
        }
    }
}