package com.example.application.usecases.user

import com.example.domain.models.UserId
import com.example.domain.repositories.UserRepository

class DeleteUserUseCase (
    private val userRepository: UserRepository,
) {
    suspend fun execute(userId: String): Result<Boolean> {
        return try {
            val userIdObj = UserId.of(userId)

            val user = userRepository.findById(userIdObj)
                ?: return Result.failure(IllegalArgumentException("User not found"))

            val deleted = userRepository.delete(userIdObj)

            if (deleted) {
                Result.success(true)
            } else {
                Result.failure(IllegalStateException("Failed to delete user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}