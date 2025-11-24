package com.example.application.usecases.user

import com.example.domain.models.AuthCredential
import com.example.domain.models.PrivacySetting
import com.example.domain.models.User
import com.example.domain.repositories.AuthCredentialRepository
import com.example.domain.repositories.UserRepository
import com.example.application.dto.RegisterUserWithPasswordDto
import com.example.application.dto.UserWithAuthDto
import kotlinx.datetime.Clock

// パスワード認証でユーザー登録
class RegisterUserWithPasswordUseCase(
    private val userRepository: UserRepository,
    private val authCredentialRepository: AuthCredentialRepository
) {
    suspend fun execute(dto: RegisterUserWithPasswordDto): Result<UserWithAuthDto> {
        return try {
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

            val passwordHash = hashPassword(dto.password)

            // 認証情報を作成
            val authCredential = AuthCredential.createPasswordAuth(
                userId = savedUser.id,
                passwordHash = passwordHash,
                now = now
            )

            val savedAuthCredential = authCredentialRepository.save(authCredential)

            Result.success(
                UserWithAuthDto.from(savedUser, listOf(savedAuthCredential))
            )
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    private fun hashPassword(password: String): String {
        // TODO: 実際にはBCryptやArgon2を使用してハッシュ化
        // 例: BCrypt.hashpw(password, BCrypt.gensalt())
        return "hashed_$password" // プレースホルダー
    }
}