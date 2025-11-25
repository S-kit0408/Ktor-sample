package com.example.application.usecases.user

import com.example.domain.repositories.UserRepository
import com.example.application.dto.UpdateUserDto
import com.example.application.dto.UserDto

class UpdateUserUseCase (
    private val userRepository: UserRepository,
) {
    suspend fun execute(userId: String, dto: UpdateUserDto): Result<UserDto> {
        return runCatching {
            val user = userRepository.findById(userId)
                ?: throw IllegalArgumentException("User not found")

            var updatedUser = user

            dto.name?.let {
//                userDomainService.validateUserName(newName)
                updatedUser = updatedUser.updateName(it)
            }

            dto.avatarUrl?.let {
                updatedUser = updatedUser.updateAvatarUrl(it)
            }

            dto.defaultPrivacySetting?.let {
                updatedUser = updatedUser.updatePrivacySetting(it)
            }

            val savedUser = userRepository.update(updatedUser)
            UserDto.from(savedUser)
        }
    }
}