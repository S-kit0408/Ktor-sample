package com.example.application.usecases

import com.example.application.dto.UserDto
import com.example.domain.repositories.UserRepository

class ListUsersUseCase(
    private val userRepository: UserRepository
) {
    suspend fun execute(): List<UserDto> {
        val users = userRepository.findAll()
        return users.map { UserDto.from(it) }
    }
}