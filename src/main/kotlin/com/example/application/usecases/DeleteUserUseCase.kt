package com.example.application.usecases

import com.example.domain.repositories.UserRepository
import com.example.domain.models.UserId

class DeleteUserUseCase (
    private val userRepository: UserRepository,
) {
    suspend fun execute(userId: String): Boolean {
        return userRepository.delete(UserId.of(userId))
    }
}