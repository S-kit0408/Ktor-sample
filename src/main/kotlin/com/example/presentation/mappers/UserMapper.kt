package com.example.presentation.mappers

import com.example.application.dto.CreateUserDto
import com.example.application.dto.UpdateUserDto
import com.example.application.dto.UserDto
import com.example.presentation.models.requests.CreateUserRequest
import com.example.presentation.models.requests.UpdateUserRequest
import com.example.presentation.models.responses.UserResponse


object UserMapper {
    fun toDto(request: CreateUserRequest): CreateUserDto {
        return CreateUserDto(
            email = request.email,
            name = request.name
        )
    }

    fun toDto(request: UpdateUserRequest): UpdateUserDto {
        return UpdateUserDto(
            name = request.name
        )
    }

    fun toResponse(dto: UserDto): UserResponse {
        return UserResponse.from(dto)
    }

    fun toResponseList(dtoList: List<UserDto>): List<UserResponse> {
        return dtoList.map { toResponse(it) }
    }
}