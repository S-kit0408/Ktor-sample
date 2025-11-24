package com.example.application.usecases.auth

import com.example.application.dto.RegisterUserWithGoogleDto
import com.example.application.dto.UserWithAuthDto
import com.example.domain.models.AuthCredential
import com.example.domain.models.PrivacySetting
import com.example.domain.models.User
import com.example.domain.repositories.AuthCredentialRepository
import com.example.domain.repositories.UserRepository
import kotlinx.datetime.Clock

// Google OAuthでユーザーを登録するユースケース

class RegisterUserWithGoogleUseCase(
    private val userRepository: UserRepository,
    private val authCredentialRepository: AuthCredentialRepository
) {
    suspend fun execute(dto: RegisterUserWithGoogleDto): Result<UserWithAuthDto> {
        return try {
            val existingAuth = authCredentialRepository.findByOAuthProviderId(
                provider = "google",
                providerId = dto.googleId
            )
            if (existingAuth != null) {
                return Result.failure(IllegalArgumentException("Google account already registered"))
            }

            if (userRepository.existsByEmail(dto.email)) {
                return Result.failure(IllegalArgumentException("Email already exists"))
            }

            val now = Clock.System.now()
            val privacySetting = dto.defaultPrivacySetting?.let {
                PrivacySetting.fromString(it)
            } ?: PrivacySetting.PRIVATE

            val user = User.create(
                email = dto.email,
                name = dto.name,
                avatarUrl = dto.avatarUrl,
                defaultPrivacySetting = privacySetting,
                now = now
            )

            val savedUser = userRepository.save(user)

            // Google認証情報を作成
            val authCredential = AuthCredential.createGoogleAuth(
                userId = savedUser.id,
                googleId = dto.googleId,
                now = now
            )

            val savedAuthCredential = authCredentialRepository.save(authCredential)

            Result.success(
                UserWithAuthDto.from(savedUser, listOf(savedAuthCredential))
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}