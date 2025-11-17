package com.example.presentation.models.responses

import com.example.application.dto.UserDto
import kotlinx.serialization.Serializable


@Serializable
data class UserResponse(
    val id: String,
    val email: String,
    val name: String,
    val createdAt: String,
    val updatedAt: String
) {
    companion object {
        fun from(dto: UserDto): UserResponse {
            return UserResponse(
                id = dto.id,
                email = dto.email,
                name = dto.name,
                createdAt = dto.createdAt.toString(),
                updatedAt = dto.updatedAt.toString()
            )
        }
    }
}


@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null
)


@Serializable
data class ErrorResponse(
    val error: String,
    val message: String
)