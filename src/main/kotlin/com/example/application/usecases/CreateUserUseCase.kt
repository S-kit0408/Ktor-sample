package com.example.application.usecases

import com.example.application.dto.CreateUserDto
import com.example.application.dto.UserDto
import com.example.domain.services.UserDomainService
import com.example.domain.repositories.UserRepository
import com.example.domain.models.User
import kotlinx.datetime.Clock

class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val userDomainService: UserDomainService
) {
    suspend fun execute(dto: CreateUserDto): UserDto {
        userDomainService.validateUserName(dto.name)
        userDomainService.checkEmailDuplicatoin(dto.email)

        val now = Clock.System.now()
        val user = User.create(
            email = dto.email,
            name = dto.name,
            now = now,
        )

        val savedUser = userRepository.save(user)

        return UserDto.from(savedUser)
    }
}