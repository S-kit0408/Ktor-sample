package com.example.infrastructure.database.repositories

import com.example.domain.models.AuthProvider
import com.example.domain.models.PrivacySetting
import com.example.domain.models.User
import com.example.domain.models.UserId
import com.example.domain.models.Email
import com.example.domain.repositories.UserRepository
import com.example.infrastructure.database.DatabaseFactory.dbQuery
import com.example.infrastructure.database.tables.UsersTable
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

// UserRepositoryの実装
class UserRepositoryImpl : UserRepository {

    override suspend fun findById(id: String): User? = dbQuery {
        UsersTable.selectAll()
            .where { UsersTable.id eq id }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    override suspend fun findByClerkUserId(clerkUserId: String): User? = dbQuery {
        UsersTable.selectAll()
            .where { UsersTable.clerkUserId eq clerkUserId }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    override suspend fun findByEmail(email: String): User? = dbQuery {
        UsersTable.selectAll()
            .where { UsersTable.email eq email }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    override suspend fun create(user: User): User = dbQuery {
        UsersTable.insert {
            it[id] = user.id.value
            it[clerkUserId] = user.clerkUserId
            it[email] = user.email.value
            it[name] = user.name
            it[avatarUrl] = user.avatarUrl
            it[primaryAuthProvider] = user.primaryAuthProvider.name.lowercase()
            it[defaultPrivacySetting] = user.defaultPrivacySetting.name.lowercase()
            it[lastLoginAt] = user.lastLoginAt
            it[createdAt] = user.createdAt
            it[updatedAt] = user.updatedAt
        }
        user
    }

    override suspend fun update(user: User): User = dbQuery {
        UsersTable.update({ UsersTable.id eq user.id.value }) {
            it[clerkUserId] = user.clerkUserId
            it[email] = user.email.value
            it[name] = user.name
            it[avatarUrl] = user.avatarUrl
            it[primaryAuthProvider] = user.primaryAuthProvider.name.lowercase()
            it[defaultPrivacySetting] = user.defaultPrivacySetting.name.lowercase()
            it[lastLoginAt] = user.lastLoginAt
            it[updatedAt] = user.updatedAt
        }
        user
    }

    override suspend fun delete(id: String): Boolean = dbQuery {
        UsersTable.deleteWhere { UsersTable.id eq id } > 0
    }

    override suspend fun list(): List<User> = dbQuery {
        UsersTable.selectAll().map { rowToUser(it) }
    }

    // ResultRowをUserエンティティに変換
    private fun rowToUser(row: ResultRow): User {
        return User(
            id = UserId.of(row[UsersTable.id]),
            clerkUserId = row[UsersTable.clerkUserId],
            email = Email(row[UsersTable.email]),
            name = row[UsersTable.name],
            avatarUrl = row[UsersTable.avatarUrl],
            primaryAuthProvider = AuthProvider.valueOf(row[UsersTable.primaryAuthProvider].uppercase()),
            defaultPrivacySetting = PrivacySetting.valueOf(row[UsersTable.defaultPrivacySetting].uppercase()),
            lastLoginAt = row[UsersTable.lastLoginAt],
            createdAt = row[UsersTable.createdAt],
            updatedAt = row[UsersTable.updatedAt],
        )
    }
}