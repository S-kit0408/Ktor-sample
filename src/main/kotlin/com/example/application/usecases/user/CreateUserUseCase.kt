package com.example.application.usecases.user

import com.example.application.dto.CreateUserDto
import com.example.application.dto.UserDto
import com.example.domain.models.User
import com.example.domain.models.PrivacySetting
import com.example.domain.models.AuthProvider
import com.example.domain.repositories.UserRepository
import kotlinx.datetime.Clock

class CreateUserUseCase(
    private val userRepository: UserRepository,
) {
    suspend fun execute(dto: CreateUserDto, clerkUserId: String): Result<UserDto> {
        return runCatching {
            val now = Clock.System.now()

            val user = User.create(
                clerkUserId = clerkUserId,
                email = dto.email,
                name = dto.name,
                avatarUrl = dto.avatarUrl,
                primaryAuthProvider = AuthProvider.UNKNOWN,
                defaultPrivacySetting = dto.defaultPrivacySetting
                    ?: PrivacySetting.PRIVATE,
                now = now
            )

            val createdUser = userRepository.create(user)
            UserDto.from(createdUser)
        }
    }
}