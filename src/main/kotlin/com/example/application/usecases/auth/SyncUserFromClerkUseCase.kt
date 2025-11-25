package com.example.application.usecases.auth

import com.example.application.dto.UserDto
import com.example.domain.models.PrivacySetting
import com.example.domain.models.User
import com.example.domain.repositories.UserRepository
import com.example.infrastructure.external.ClerkApiClient
import kotlinx.datetime.Clock

class SyncUserFromClerkUseCase(
    private val userRepository: UserRepository,
    private val clerkApiClient: ClerkApiClient
) {
    suspend fun execute(clerkUserId: String): Result<UserDto> {
        return runCatching {
            val clerkUser = clerkApiClient.getUserById(clerkUserId)
                ?: throw IllegalStateException("Clerk user not found: $clerkUserId")

            val existingUser = userRepository.findByClerkUserId(clerkUserId)
            val now = Clock.System.now()

            val user = if (existingUser != null) {
                val updatedUser = existingUser.copy(
                    email = com.example.domain.models.Email(clerkUser.primaryEmail),
                    name = clerkUser.fullName,
                    avatarUrl = clerkUser.image_url,
                    lastLoginAt = now,
                    updatedAt = now
                )
                userRepository.update(updatedUser)
                updatedUser
            } else {
                val newUser = User.create(
                    clerkUserId = clerkUserId,
                    email = clerkUser.primaryEmail,
                    name = clerkUser.fullName,
                    avatarUrl = clerkUser.image_url,
                    primaryAuthProvider = clerkUser.authProvider,
                    defaultPrivacySetting = PrivacySetting.PRIVATE,
                    now = now
                )
                userRepository.create(newUser)
                newUser
            }

            UserDto.from(user)
        }
    }
}