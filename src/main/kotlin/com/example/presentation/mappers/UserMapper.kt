package com.example.presentation.mappers

import com.example.application.dto.CreateUserDto
import com.example.application.dto.UpdateUserDto
import com.example.application.dto.UserDto
import com.example.domain.models.PrivacySetting
import com.example.presentation.models.requests.CreateUserRequest
import com.example.presentation.models.requests.UpdateUserRequest
import com.example.presentation.models.responses.UserResponse


object UserMapper {

    // UserDto -> UserResponse
    fun toResponse(userDto: UserDto): UserResponse {
        return UserResponse(
            id = userDto.id,
            clerkUserId = userDto.clerkUserId,
            email = userDto.email,
            name = userDto.name,
            avatarUrl = userDto.avatarUrl,
            primaryAuthProvider = userDto.primaryAuthProvider.name.lowercase(),
            defaultPrivacySetting = userDto.defaultPrivacySetting.name.lowercase(),
            lastLoginAt = userDto.lastLoginAt,
            createdAt = userDto.createdAt,
            updatedAt = userDto.updatedAt
        )
    }

    // List<UserDto> -> List<UserResponse>
    fun toResponseList(userDtos: List<UserDto>): List<UserResponse> {
        return userDtos.map { toResponse(it) }
    }

    // CreateUserRequest -> CreateUserDto
    fun toDto(request: CreateUserRequest): CreateUserDto {
        return CreateUserDto(
            email = request.email,
            name = request.name,
            avatarUrl = request.avatarUrl,
            defaultPrivacySetting = request.defaultPrivacySetting?.let {
                PrivacySetting.fromString(it)
            }
        )
    }

    // UpdateUserRequest -> UpdateUserDto
    fun toDto(request: UpdateUserRequest): UpdateUserDto {
        return UpdateUserDto(
            name = request.name,
            avatarUrl = request.avatarUrl,
            defaultPrivacySetting = request.defaultPrivacySetting?.let {
                PrivacySetting.fromString(it)
            }
        )
    }
}