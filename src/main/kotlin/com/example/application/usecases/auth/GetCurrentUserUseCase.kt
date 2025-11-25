package com.example.application.usecases.auth

import com.example.application.dto.UserDto
import com.example.domain.repositories.UserRepository

class GetCurrentUserUseCase(
    private val userRepository: UserRepository
) {
    suspend fun execute(clerkUserId: String): Result<UserDto> {
        return runCatching {
            val user = userRepository.findByClerkUserId(clerkUserId)
                ?: throw NoSuchElementException("User not found with clerkUserId: $clerkUserId")

            UserDto.from(user)
        }
    }
}