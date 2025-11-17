package com.example.infrastructure.database.repositories

import com.example.domain.models.Email
import com.example.domain.models.User
import com.example.domain.models.UserId
import com.example.domain.repositories.UserRepository
import com.example.infrastructure.database.DatabaseFactory.dbQuery
import com.example.infrastructure.database.tables.UsersTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

// UserRepositoryの実装

class UserRepositoryImpl : UserRepository {

    override suspend fun findById(id: UserId): User? = dbQuery {
        UsersTable.selectAll()
            .where { UsersTable.id eq id.value }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    override suspend fun findByEmail(email: String): User? = dbQuery {
        UsersTable.selectAll()
            .where { UsersTable.email eq email }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    override suspend fun findAll(): List<User> = dbQuery {
        UsersTable.selectAll()
            .map { rowToUser(it) }
    }

    override suspend fun save(user: User): User = dbQuery {
        val exists = UsersTable.selectAll()
            .where { UsersTable.id eq user.id.value }
            .count() > 0

        if (exists) {
            // 更新
            UsersTable.update({ UsersTable.id eq user.id.value }) {
                it[email] = user.email.value
                it[name] = user.name
                it[updatedAt] = user.updatedAt
            }
        } else {
            // 新規作成
            UsersTable.insert {
                it[id] = user.id.value
                it[email] = user.email.value
                it[name] = user.name
                it[createdAt] = user.createdAt
                it[updatedAt] = user.updatedAt
            }
        }
        user
    }

    override suspend fun delete(id: UserId): Boolean = dbQuery {
        val deletedCount = UsersTable.deleteWhere { UsersTable.id eq id.value }
        deletedCount > 0
    }

    override suspend fun existsByEmail(email: String): Boolean = dbQuery {
        UsersTable.selectAll()
            .where { UsersTable.email eq email }
            .count() > 0
    }


    // ResultRowをUserエンティティに変換
    private fun rowToUser(row: ResultRow): User {
        return User(
            id = UserId.of(row[UsersTable.id]),
            email = Email(row[UsersTable.email]),
            name = row[UsersTable.name],
            createdAt = row[UsersTable.createdAt],
            updatedAt = row[UsersTable.updatedAt]
        )
    }
}