package com.example.presentation.mappers

import com.example.application.dto.*
import com.example.presentation.models.requests.*
import com.example.presentation.models.responses.*

object AuthMapper {

    fun toRegisterPasswordDto(request: RegisterUserRequest): RegisterUserWithPasswordDto {
        require(request.password != null) { "Password is required" }
        return RegisterUserWithPasswordDto(
            email = request.email,
            name = request.name,
            password = request.password,
            avatarUrl = request.avatarUrl,
            defaultPrivacySetting = request.defaultPrivacy
        )
    }

    fun toRegisterGoogleDto(request: RegisterUserRequest): RegisterUserWithGoogleDto {
//        require(request.googleId != null) { "Google ID is required" }
        val googleId = request.googleId
            ?: throw IllegalArgumentException("Google ID is required")

        return RegisterUserWithGoogleDto(
            email = request.email,
            name = request.name,
            googleId = googleId,
            avatarUrl = request.avatarUrl,
            defaultPrivacySetting = request.defaultPrivacy
        )
    }

    fun toLoginPasswordDto(request: LoginRequest): LoginWithPasswordDto {
        require(request.email != null && request.password != null) {
            "Email and password are required"
        }
        return LoginWithPasswordDto(
            email = request.email,
            password = request.password
        )
    }

    fun toLoginGoogleDto(request: LoginRequest): LoginWithGoogleDto {
        require(request.googleId != null) { "Google ID is required" }
        return LoginWithGoogleDto(
            googleId = request.googleId,
            email = request.email
        )
    }

    fun toChangePasswordDto(request: ChangePasswordRequest): ChangePasswordDto {
        return ChangePasswordDto(
            userId = request.userId,
            currentPassword = request.currentPassword,
            newPassword = request.newPassword
        )
    }

    // DTO → Response
    fun toAuthResponse(dto: AuthenticationResponseDto): AuthResponse {
        return AuthResponse(
            user = UserMapper.toResponse(dto.user),
            authMethods = dto.authMethods.map { toAuthMethodResponse(it) },
            message = dto.message
        )
    }

    fun toUserWithAuthResponse(dto: UserWithAuthDto): UserWithAuthResponse {
        return UserWithAuthResponse(
            user = UserMapper.toResponse(dto.user),
            authMethods = dto.authMethods.map { toAuthMethodResponse(it) }
        )
    }

    private fun toAuthMethodResponse(dto: AuthCredentialDto): AuthMethodResponse {
        return AuthMethodResponse(
            id = dto.id,
            authType = dto.authType.name.lowercase(),
            oauthProvider = dto.oauthProvider,
            createdAt = dto.createdAt
        )
    }
}