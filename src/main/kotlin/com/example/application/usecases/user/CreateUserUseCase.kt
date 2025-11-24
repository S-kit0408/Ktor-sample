package com.example.application.usecases.user

import com.example.application.dto.CreateUserDto
import com.example.application.dto.UserDto
import com.example.domain.models.User
import com.example.domain.models.PrivacySetting
import com.example.domain.repositories.UserRepository
import com.example.domain.services.UserDomainService
import kotlinx.datetime.Clock

class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val userDomainService: UserDomainService
) {
    suspend fun execute(dto: CreateUserDto): Result<UserDto> {
        return try {
            userDomainService.validateUserName(dto.name)
            userDomainService.checkEmailDuplicatoin(dto.email)

            val now = Clock.System.now()
            val user = User.create(
                email = dto.email,
                name = dto.name,
                avatarUrl = dto.avatarUrl,
                defaultPrivacySetting = dto.defaultPrivacySetting ?: PrivacySetting.PRIVATE,
                now = now,
            )

            val savedUser = userRepository.save(user)

            Result.success(UserDto.from(savedUser))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}