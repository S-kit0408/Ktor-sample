package com.example.presentation.controllers

import com.example.application.usecases.*
import com.example.domain.services.EmailAlreadyExistsException
import com.example.presentation.mappers.UserMapper
import com.example.presentation.models.requests.CreateUserRequest
import com.example.presentation.models.requests.UpdateUserRequest
import com.example.presentation.models.responses.ApiResponse
import com.example.presentation.models.responses.ErrorResponse
import com.example.presentation.models.responses.UserResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

// ユーザーコントローラー
// HTTPリクエスト/レスポンスの処理のみを担当
class UserController(
    private val createUserUseCase: CreateUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val listUsersUseCase: ListUsersUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) {
    // POST /api/users
    suspend fun createUser(call: ApplicationCall) {
        try {
            val request = call.receive<CreateUserRequest>()
            val dto = UserMapper.toDto(request)
            val userDto = createUserUseCase.execute(dto)
            val response = UserMapper.toResponse(userDto)

            call.respond(
                HttpStatusCode.Created,
                ApiResponse(
                    success = true,
                    data = response,
                    message = "User created successfully"
                )
            )
        } catch (e: EmailAlreadyExistsException) {
            call.respond(
                HttpStatusCode.Conflict,
                ErrorResponse(
                    error = "CONFLICT",
                    message = e.message ?: "Email already exists"
                )
            )
        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    error = "BAD_REQUEST",
                    message = e.message ?: "Invalid input"
                )
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    error = "INTERNAL_SERVER_ERROR",
                    message = "An unexpected error occurred"
                )
            )
        }
    }


    // GET /api/users/{id}
    suspend fun getUser(call: ApplicationCall) {
        try {
            val userId = call.parameters["id"]
                ?: return call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse(
                        error = "BAD_REQUEST",
                        message = "User ID is required"
                    )
                )

            val userDto = getUserUseCase.execute(userId)
            if (userDto == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse(
                        error = "NOT_FOUND",
                        message = "User not found"
                    )
                )
            } else {
                val response = UserMapper.toResponse(userDto)
                call.respond(
                    HttpStatusCode.OK,
                    ApiResponse(
                        success = true,
                        data = response
                    )
                )
            }
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    error = "INTERNAL_SERVER_ERROR",
                    message = "An unexpected error occurred"
                )
            )
        }
    }


    // GET /api/users
    suspend fun listUsers(call: ApplicationCall) {
        try {
            val userDtos = listUsersUseCase.execute()
            val responses = UserMapper.toResponseList(userDtos)
            call.respond(
                HttpStatusCode.OK,
                ApiResponse(
                    success = true,
                    data = responses
                )
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    error = "INTERNAL_SERVER_ERROR",
                    message = "An unexpected error occurred"
                )
            )
        }
    }


     // PUT /api/users/{id}
    suspend fun updateUser(call: ApplicationCall) {
        try {
            val userId = call.parameters["id"]
                ?: return call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse(
                        error = "BAD_REQUEST",
                        message = "User ID is required"
                    )
                )

            val request = call.receive<UpdateUserRequest>()
            val dto = UserMapper.toDto(request)
            val userDto = updateUserUseCase.execute(userId, dto)

            if (userDto == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse(
                        error = "NOT_FOUND",
                        message = "User not found"
                    )
                )
            } else {
                val response = UserMapper.toResponse(userDto)
                call.respond(
                    HttpStatusCode.OK,
                    ApiResponse(
                        success = true,
                        data = response,
                        message = "User updated successfully"
                    )
                )
            }
        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    error = "BAD_REQUEST",
                    message = e.message ?: "Invalid input"
                )
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    error = "INTERNAL_SERVER_ERROR",
                    message = "An unexpected error occurred"
                )
            )
        }
    }


    // DELETE /api/users/{id}
    suspend fun deleteUser(call: ApplicationCall) {
        try {
            val userId = call.parameters["id"]
                ?: return call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse(
                        error = "BAD_REQUEST",
                        message = "User ID is required"
                    )
                )

            val deleted = deleteUserUseCase.execute(userId)
            if (deleted) {
                call.respond(
                    HttpStatusCode.OK,
                    ApiResponse<Unit>(
                        success = true,
                        message = "User deleted successfully"
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse(
                        error = "NOT_FOUND",
                        message = "User not found"
                    )
                )
            }
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    error = "INTERNAL_SERVER_ERROR",
                    message = "An unexpected error occurred"
                )
            )
        }
    }
}