package com.example.application.dto

import com.example.domain.models.User
import com.example.common.utils.conversionJst
import com.example.domain.models.PrivacySetting
import com.example.domain.models.AuthProvider

data class UserDto(
    val id: String,
    val clerkUserId: String,
    val email: String,
    val name: String,
    val avatarUrl: String?,
    val primaryAuthProvider: AuthProvider,
    val defaultPrivacySetting: PrivacySetting,
    val lastLoginAt: String?,
    val createdAt: String,
    val updatedAt: String
) {
    companion object {
        fun from(user: User): UserDto {
            return UserDto(
                id = user.id.value,
                clerkUserId = user.clerkUserId,
                email = user.email.value,
                name = user.name,
                avatarUrl = user.avatarUrl,
                primaryAuthProvider = user.primaryAuthProvider,
                defaultPrivacySetting = user.defaultPrivacySetting,
                lastLoginAt = user.lastLoginAt?.conversionJst(),
                createdAt = user.createdAt.conversionJst(),
                updatedAt = user.updatedAt.conversionJst(),
            )
        }
    }
}

data class CreateUserDto(
    val email: String,
    val name: String,
    val avatarUrl: String? = null,
    val defaultPrivacySetting: PrivacySetting? = null,
)

data class UpdateUserDto(
    val name: String? = null,
    val avatarUrl: String? = null,
    val defaultPrivacySetting: PrivacySetting? = null,
)