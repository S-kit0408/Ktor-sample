package com.example.application.dto

import com.example.domain.models.User
import com.example.common.utils.conversionJst
import com.example.domain.models.PrivacySetting

data class UserDto(
    val id: String,
    val email: String,
    val name: String,
    val avatarUrl: String?,
    val defaultPrivacySetting: PrivacySetting,
    val lastLoginAt: String?,
    val createdAt: String,
    val updatedAt: String
) {
    companion object {
        fun from(user: User): UserDto {
            return UserDto(
                id = user.id.value,
                email = user.email.value,
                name = user.name,
                avatarUrl = user.avatarUrl,
                defaultPrivacySetting = user.defaultPrivacySetting,
                lastLoginAt = user.lastLoginAt?.conversionJst(),
                createdAt = user.createdAt.conversionJst(),
                updatedAt = user.updatedAt.conversionJst()
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