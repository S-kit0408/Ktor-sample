package com.example.application.usecases.user

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
    suspend fun execute(userId: String, dto: UpdateUserDto): Result<UserDto> {
        return try {
            val userIdObj = UserId.of(userId)

            var user = userRepository.findById(userIdObj)
                ?: return Result.failure(IllegalArgumentException("User not found"))

            dto.name?.let { newName ->
                userDomainService.validateUserName(newName)
                user = user.updateName(newName)
            }

            dto.avatarUrl?.let { newAvatarUrl ->
                user = user.updateAvatarUrl(newAvatarUrl)
            }

            dto.defaultPrivacySetting?.let { newSetting ->
                user = user.updatePrivacySetting(newSetting)
            }

            val savedUser = userRepository.save(user)

            Result.success(UserDto.from(savedUser))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}