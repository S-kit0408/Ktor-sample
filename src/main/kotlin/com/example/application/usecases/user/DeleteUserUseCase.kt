package com.example.application.usecases.user

import com.example.domain.repositories.UserRepository

class DeleteUserUseCase (
    private val userRepository: UserRepository,
) {
    suspend fun execute(userId: String): Result<Unit> {
        return runCatching {
            val deleted = userRepository.delete(userId)

            if (!deleted) {
                throw NoSuchElementException("User not found with id: $userId")
            }
        }
    }
}