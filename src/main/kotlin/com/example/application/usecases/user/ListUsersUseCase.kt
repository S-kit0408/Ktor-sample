package com.example.application.usecases.user

import com.example.application.dto.UserDto
import com.example.domain.repositories.UserRepository

class ListUsersUseCase(
    private val userRepository: UserRepository
) {
    suspend fun execute(): Result<List<UserDto>> {
        return runCatching {
            userRepository.list().map { UserDto.from(it) }
        }
    }
}