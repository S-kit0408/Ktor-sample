package com.example.presentation.models.responses

import com.example.application.dto.UserDto
import kotlinx.serialization.Serializable


@Serializable
data class UserResponse(
    val id: String,
    val clerkUserId: String,
    val email: String,
    val name: String,
    val avatarUrl: String?,
    val primaryAuthProvider: String,
    val defaultPrivacySetting: String,
    val lastLoginAt: String?,
    val createdAt: String,
    val updatedAt: String
) {
    companion object {
        fun from(dto: UserDto): UserResponse {
            return UserResponse(
                id = dto.id,
                clerkUserId = dto.clerkUserId,
                email = dto.email,
                name = dto.name,
                avatarUrl = dto.avatarUrl,
                primaryAuthProvider = dto.primaryAuthProvider.name.lowercase(),
                defaultPrivacySetting = dto.defaultPrivacySetting.name.lowercase(),
                lastLoginAt = dto.lastLoginAt,
                createdAt = dto.createdAt,
                updatedAt = dto.updatedAt
            )
        }
    }
}