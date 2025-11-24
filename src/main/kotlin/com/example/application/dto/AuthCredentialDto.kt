package com.example.application.dto

import com.example.common.utils.conversionJst
import com.example.domain.models.AuthCredential
import com.example.domain.models.AuthType
import com.example.domain.models.User
import com.example.application.dto.UserDto

data class AuthCredentialDto(
    val id: String,
    val userId: String,
    val authType: AuthType,
    val oauthProvider: String?,
    val createdAt: String,
) {
    companion object {
        fun from(authCredential: AuthCredential): AuthCredentialDto {
            return AuthCredentialDto(
                id = authCredential.id.value,
                userId = authCredential.userId.value,
                authType = authCredential.authType,
                oauthProvider = authCredential.oauthProvider,
                createdAt = authCredential.createdAt.conversionJst()
            )
        }
    }
}

// password 認証追加
data class CreatePasswordAuthDto(
    val userId: String,
    val password: String,
)

// google 認証追加
data class CreateGoogleAuthDto(
    val userId: String,
    val googleId: String,
)

// ユーザー登録（Password）
data class RegisterUserWithPasswordDto(
    val email: String,
    val name: String,
    val password: String,
    val avatarUrl: String? = null,
    val defaultPrivacySetting: String? = null
) {
    init {
        require(email.isNotBlank()) { "Email cannot be blank" }
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(name.length in 2..100) { "Name must be between 2 and 100 characters" }
        require(password.isNotBlank()) { "Password cannot be blank" }
        require(password.length >= 8) { "Password must be at least 8 characters" }
        avatarUrl?.let {
            require(it.length <= 500) { "Avatar URL cannot exceed 500 characters" }
        }
    }
}

// ユーザー登録（Google OAuth）
data class RegisterUserWithGoogleDto(
    val email: String,
    val name: String,
    val googleId: String,
    val avatarUrl: String? = null,
    val defaultPrivacySetting: String? = null
) {
    init {
        require(email.isNotBlank()) { "Email cannot be blank" }
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(name.length in 2..100) { "Name must be between 2 and 100 characters" }
        require(googleId.isNotBlank()) { "Google ID cannot be blank" }
        avatarUrl?.let {
            require(it.length <= 500) { "Avatar URL cannot exceed 500 characters" }
        }
    }
}

// ログイン（パスワード認証）
data class LoginWithPasswordDto(
    val email: String,
    val password: String
) {
    init {
        require(email.isNotBlank()) { "Email cannot be blank" }
        require(password.isNotBlank()) { "Password cannot be blank" }
    }
}

// ログイン（Google OAuth）
data class LoginWithGoogleDto(
    val googleId: String,
    val email: String? = null  // Google APIから取得したemail（オプション）
) {
    init {
        require(googleId.isNotBlank()) { "Google ID cannot be blank" }
    }
}

// ユーザーと認証情報を含む完全なDTO（レスポンス用）
data class UserWithAuthDto(
    val user: UserDto,
    val authMethods: List<AuthCredentialDto>  // ユーザーが持つ全認証方法
) {
    companion object {
        fun from(user: User, authCredentials: List<AuthCredential>): UserWithAuthDto {
            return UserWithAuthDto(
                user = UserDto.from(user),
                authMethods = authCredentials.map { AuthCredentialDto.from(it) }
            )
        }
    }
}

// 認証成功レスポンス
// 将来的にJWTトークンなどを含める
data class AuthenticationResponseDto(
    val user: UserDto,
    val authMethods: List<AuthCredentialDto>,
    val message: String = "Authentication successful"
    // val accessToken: String,  // 将来的に追加
    // val refreshToken: String, // 将来的に追加
)

// パスワード変更
data class ChangePasswordDto(
    val userId: String,
    val currentPassword: String,
    val newPassword: String
) {
    init {
        require(currentPassword.isNotBlank()) { "Current password cannot be blank" }
        require(newPassword.isNotBlank()) { "New password cannot be blank" }
        require(newPassword.length >= 8) { "New password must be at least 8 characters" }
        require(currentPassword != newPassword) { "New password must be different from current password" }
    }
}