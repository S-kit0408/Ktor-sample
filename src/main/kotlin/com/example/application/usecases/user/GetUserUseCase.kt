package com.example.application.usecases.user

import com.example.application.dto.UserDto
import com.example.domain.models.UserId
import com.example.domain.repositories.UserRepository

class GetUserUseCase (
    private val userRepository: UserRepository
) {
    suspend fun execute(userId: String): Result<UserDto> {
        return try {
            val user = userRepository.findById(UserId.Companion.of(userId))

            if (user == null) return Result.failure(IllegalArgumentException("User not found"))

            Result.success(UserDto.from(user))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}