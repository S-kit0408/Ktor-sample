package com.example.presentation.controllers

import com.example.application.usecases.user.CreateUserUseCase
import com.example.application.usecases.user.DeleteUserUseCase
import com.example.application.usecases.user.GetUserUseCase
import com.example.application.usecases.user.ListUsersUseCase
import com.example.application.usecases.user.UpdateUserUseCase
import com.example.presentation.mappers.UserMapper
import com.example.presentation.models.requests.CreateUserRequest
import com.example.presentation.models.requests.UpdateUserRequest
import com.example.presentation.models.responses.ApiResponse
import com.example.presentation.models.responses.ErrorResponse
import com.example.common.utils.ErrorHandle
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

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

            createUserUseCase.execute(dto)
                .onSuccess { userDto ->
                    val response = UserMapper.toResponse(userDto)
                    call.respond(
                        HttpStatusCode.Created,
                        ApiResponse(
                            success = true,
                            data = response,
                            message = "User created successfully"
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

            getUserUseCase.execute(userId)
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

    // GET /api/users
    suspend fun listUsers(call: ApplicationCall) {
        try {
            listUsersUseCase.execute()
                .onSuccess { userDtos ->
                    val responses = UserMapper.toResponseList(userDtos)
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse(
                            success = true,
                            data = responses
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

            updateUserUseCase.execute(userId, dto)
                .onSuccess { userDto ->
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
                .onFailure { exception ->
                    ErrorHandle(call, exception)
                }
        } catch (e: Exception) {
            ErrorHandle(call, e)
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

            deleteUserUseCase.execute(userId)
                .onSuccess {
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse<Unit>(
                            success = true,
                            message = "User deleted successfully"
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