package com.example.application.usecases.auth

import com.example.application.dto.AuthenticationResponseDto
import com.example.application.dto.AuthCredentialDto
import com.example.application.dto.LoginWithPasswordDto
import com.example.application.dto.UserDto
import com.example.domain.models.AuthType
import com.example.domain.repositories.AuthCredentialRepository
import com.example.domain.repositories.UserRepository
import kotlinx.datetime.Clock

// パスワード認証でログインするユースケース
class LoginWithPasswordUseCase(
    private val userRepository: UserRepository,
    private val authCredentialRepository: AuthCredentialRepository
) {
    suspend fun execute(dto: LoginWithPasswordDto): Result<AuthenticationResponseDto> {
        return try {
            val user = userRepository.findByEmail(dto.email)
                ?: return Result.failure(IllegalArgumentException("Invalid email or password"))

            val authCredential = authCredentialRepository.findByUserIdAndAuthType(
                userId = user.id,
                authType = AuthType.PASSWORD
            ) ?: return Result.failure(IllegalArgumentException("Invalid email or password"))

            if (!verifyPassword(dto.password, authCredential.passwordHash!!)) {
                return Result.failure(IllegalArgumentException("Invalid email or password"))
            }

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

    // パスワードを検証
    private fun verifyPassword(plainPassword: String, hashedPassword: String): Boolean {
        // TODO: 実際にはBCryptやArgon2を使用して検証
        // 例: BCrypt.checkpw(plainPassword, hashedPassword)
        return hashedPassword == "hashed_$plainPassword" // プレースホルダー
    }
}