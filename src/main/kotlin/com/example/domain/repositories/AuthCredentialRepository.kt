package com.example.domain.repositories

import com.example.domain.models.AuthCredential
import com.example.domain.models.AuthCredentialId
import com.example.domain.models.AuthType
import com.example.domain.models.UserId

interface AuthCredentialRepository {

    suspend fun findById(id: AuthCredentialId): AuthCredential?
    suspend fun findByUserId(userId: UserId): List<AuthCredential>
    suspend fun findByUserIdAndAuthType(userId: UserId, authType: AuthType): AuthCredential?
    suspend fun findByOAuthProviderId(provider: String, providerId: String): AuthCredential?
    suspend fun save(authCredential: AuthCredential): AuthCredential
    suspend fun delete(id: AuthCredentialId): Boolean
    suspend fun existsByUserIdAndAuthType(userId: UserId, authType: AuthType): Boolean
    suspend fun existsByOAuthProvider(provider: String, providerId: String): Boolean
}