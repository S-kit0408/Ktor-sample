package com.example.application.dto

import com.example.domain.models.User
import com.example.common.utils.toIso8601Jst

data class UserDto(
    val id: String,
    val email: String,
    val name: String,
    val createdAt: String,
    val updatedAt: String
) {
    companion object {
        fun from(user: User): UserDto {

            return UserDto(
                id = user.id.value,
                email = user.email.value,
                name = user.name,
                createdAt = user.createdAt.toIso8601Jst(),
                updatedAt = user.updatedAt.toIso8601Jst()
            )
        }
    }
}

data class CreateUserDto(
    val email: String,
    val name: String,
)

data class UpdateUserDto(
    val name: String
)