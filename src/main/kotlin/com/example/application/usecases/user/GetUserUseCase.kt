package com.example.application.usecases.user

import com.example.application.dto.UserDto
import com.example.domain.models.UserId
import com.example.domain.repositories.UserRepository

class GetUserUseCase (
    private val userRepository: UserRepository
) {
    suspend fun execute(userId: String): Result<UserDto> {
        return runCatching {
            val user = userRepository.findById(userId)
                ?: throw NoSuchElementException("User not found with id: $userId")

            UserDto.from(user)
        }
    }
}