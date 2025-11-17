package com.example.application.usecases

import com.example.domain.repositories.UserRepository
import com.example.domain.services.UserDomainService
import com.example.domain.models.UserId
import com.example.application.dto.UpdateUserDto
import com.example.application.dto.UserDto
import kotlinx.datetime.Clock

class UpdateUserUseCase (
    private val userRepository: UserRepository,
    private val userDomainService: UserDomainService
) {
    suspend fun execute(userId: String, dto: UpdateUserDto): UserDto? {
        userDomainService.validateUserName(dto.name)

        val existingUser = userRepository.findById(UserId.of(userId)) ?: return null

        val updatedUser = existingUser.copy(
            name = dto.name,
            updatedAt = Clock.System.now()
        )

        val savedUser = userRepository.save(updatedUser)

        return UserDto.from(savedUser)
    }
}