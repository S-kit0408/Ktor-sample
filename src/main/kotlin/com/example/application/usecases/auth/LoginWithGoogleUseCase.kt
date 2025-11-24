package com.example.application.usecases.auth

import com.example.application.dto.AuthenticationResponseDto
import com.example.application.dto.LoginWithGoogleDto
import com.example.application.dto.UserDto
import com.example.application.dto.AuthCredentialDto
import com.example.domain.repositories.AuthCredentialRepository
import com.example.domain.repositories.UserRepository
import kotlinx.datetime.Clock

// Google OAuthでログインするユースケース
class LoginWithGoogleUseCase(
    private val userRepository: UserRepository,
    private val authCredentialRepository: AuthCredentialRepository
) {
    suspend fun execute(dto: LoginWithGoogleDto): Result<AuthenticationResponseDto> {
        return try {
            //Google認証情報の検索
            val authCredential = authCredentialRepository.findByOAuthProviderId(
                provider = "google",
                providerId = dto.googleId
            ) ?: return Result.failure(IllegalArgumentException("Google account not found. Please register first."))


            val user = userRepository.findById(authCredential.userId)
                ?: return Result.failure(IllegalStateException("User not found"))

            // 最終ログイン日時を更新
            val now = Clock.System.now()
            val updatedUser = user.recordLogin(now)
            userRepository.save(updatedUser)

            // ユーザーの全認証方法を取得
            val authCredentials = authCredentialRepository.findByUserId(user.id)

            Result.success(
                AuthenticationResponseDto(
                    user = UserDto.from(updatedUser),
                    authMethods = authCredentials.map {
                        AuthCredentialDto.from(it)
                    },
                    message = "Login successful"
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}