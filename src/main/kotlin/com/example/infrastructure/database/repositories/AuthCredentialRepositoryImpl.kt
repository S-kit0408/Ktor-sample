package com.example.infrastructure.database.repositories

import com.example.domain.models.AuthCredential
import com.example.domain.models.AuthCredentialId
import com.example.domain.models.AuthType
import com.example.domain.models.UserId
import com.example.domain.repositories.AuthCredentialRepository
import com.example.infrastructure.database.DatabaseFactory.dbQuery
import com.example.infrastructure.database.tables.AuthCredentialsTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

// AuthCredentialRepositoryllの実装

class AuthCredentialRepositoryImpl : AuthCredentialRepository {

    override suspend fun findById(id: AuthCredentialId): AuthCredential? = dbQuery {
        AuthCredentialsTable.selectAll()
            .where { AuthCredentialsTable.id eq id.value }
            .map { rowToAuthCredential(it) }
            .singleOrNull()
    }

    override suspend fun findByUserId(userId: UserId): List<AuthCredential> = dbQuery {
        AuthCredentialsTable.selectAll()
            .where { AuthCredentialsTable.userId eq userId.value }
            .map { rowToAuthCredential(it) }
    }

    override suspend fun findByUserIdAndAuthType(
        userId: UserId,
        authType: AuthType
    ): AuthCredential? = dbQuery {
        AuthCredentialsTable.selectAll()
            .where {
                (AuthCredentialsTable.userId eq userId.value) and
                (AuthCredentialsTable.authType eq authType.name.lowercase())
            }
            .map { rowToAuthCredential(it) }
            .singleOrNull()

    }

    override suspend fun findByOAuthProviderId(
        provider: String,
        providerId: String
    ): AuthCredential? = dbQuery {
        AuthCredentialsTable.selectAll()
            .where {
                (AuthCredentialsTable.oauthProvider eq provider) and
                (AuthCredentialsTable.oauthProviderId eq providerId)
            }
            .map { rowToAuthCredential(it) }
            .singleOrNull()
    }

    override suspend fun save(authCredential: AuthCredential): AuthCredential = dbQuery {
        AuthCredentialsTable.insert {
            it[id] = authCredential.id.value
            it[userId] = authCredential.userId.value
            it[authType] = authCredential.authType.name.lowercase()
            it[passwordHash] = authCredential.passwordHash
            it[oauthProvider] = authCredential.oauthProvider
            it[oauthProviderId] = authCredential.oauthProviderId
            it[createdAt] = authCredential.createdAt
        }
        authCredential
    }

    override suspend fun delete(id: AuthCredentialId): Boolean = dbQuery {
        val deletedCount = AuthCredentialsTable.deleteWhere {
            AuthCredentialsTable.id eq id.value
        }
        deletedCount > 0
    }

    override suspend fun existsByUserIdAndAuthType(
        userId: UserId,
        authType: AuthType
    ): Boolean = dbQuery {
        AuthCredentialsTable.selectAll()
            .where {
                (AuthCredentialsTable.userId eq userId.value) and
                        (AuthCredentialsTable.authType eq authType.name.lowercase())
            }
            .count() > 0
    }

    override suspend fun existsByOAuthProvider(
        provider: String,
        providerId: String
    ): Boolean = dbQuery {
        AuthCredentialsTable.selectAll()
            .where {
                (AuthCredentialsTable.oauthProvider eq provider) and
                (AuthCredentialsTable.oauthProviderId eq providerId)
            }
            .count() > 0
    }

    private fun rowToAuthCredential(row: ResultRow): AuthCredential {
        return AuthCredential(
            id = AuthCredentialId.of(row[AuthCredentialsTable.id]),
            userId = UserId.of(row[AuthCredentialsTable.userId]),
            authType = AuthType.fromString(row[AuthCredentialsTable.authType]),
            passwordHash = row[AuthCredentialsTable.passwordHash],
            oauthProvider = row[AuthCredentialsTable.oauthProvider],
            oauthProviderId = row[AuthCredentialsTable.oauthProviderId],
            createdAt = row[AuthCredentialsTable.createdAt]
        )
    }
}