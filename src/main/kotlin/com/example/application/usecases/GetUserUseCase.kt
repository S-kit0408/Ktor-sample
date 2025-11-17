package com.example.application.usecases

import com.example.application.dto.UserDto
import com.example.domain.models.UserId
import com.example.domain.repositories.UserRepository

class GetUserUseCase (
    private val userRepository: UserRepository
) {
    suspend fun execute(userId: String): UserDto? {
        val user = userRepository.findById(UserId.of(userId))
        return user?.let { UserDto.from(it) }
    }
}