package com.example.application.usecases.user

import com.example.application.dto.UserDto
import com.example.domain.repositories.UserRepository

class ListUsersUseCase(
    private val userRepository: UserRepository
) {
    suspend fun execute(): Result<List<UserDto>> {
        return try {
            val users = userRepository.findAll()
            val UserDtos = users.map { UserDto.from(it) }

            Result.success(UserDtos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}