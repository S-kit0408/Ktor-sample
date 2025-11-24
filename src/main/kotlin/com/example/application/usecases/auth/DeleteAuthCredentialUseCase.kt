package com.example.application.usecases.auth

import com.example.domain.models.AuthCredentialId
import com.example.domain.models.UserId
import com.example.domain.repositories.AuthCredentialRepository

// 認証方法を削除するユースケース
// 例: Googleアカウント連携を解除
class DeleteAuthCredentialUseCase(
    private val authCredentialRepository: AuthCredentialRepository
) {
    suspend fun execute(authCredentialId: String, userId: String): Result<Boolean> {
        return try {
            val credentialId = AuthCredentialId.of(authCredentialId)
            val userIdObj = UserId.of(userId)

            // 1. 認証情報を取得
            val authCredential = authCredentialRepository.findById(credentialId)
                ?: return Result.failure(IllegalArgumentException("Auth credential not found"))

            // 2. 所有者確認
            if (authCredential.userId != userIdObj) {
                return Result.failure(IllegalArgumentException("Unauthorized"))
            }

            // 3. 最後の認証方法でないか確認
            val userAuthMethods = authCredentialRepository.findByUserId(userIdObj)
            if (userAuthMethods.size <= 1) {
                return Result.failure(
                    IllegalStateException("Cannot delete the last authentication method")
                )
            }

            // 4. 削除
            val deleted = authCredentialRepository.delete(credentialId)

            Result.success(deleted)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}