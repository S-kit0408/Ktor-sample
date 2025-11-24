package com.example.infrastructure.database.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

// AuthCredentialsテーブル定義
object AuthCredentialsTable : Table("auth_credentials") {
    val id = varchar("id", 26)
    val userId = varchar("user_id", 26).references(UsersTable.id, onDelete = ReferenceOption.CASCADE)
    val authType = varchar("auth_type", 20)

    val passwordHash = varchar("password_hash", 255).nullable()

    val oauthProvider = varchar("oauth_provider", 50).nullable()
    val oauthProviderId = varchar("oauth_provider_id", 255).nullable()

    val createdAt = timestamp("created_at")

    override val primaryKey = PrimaryKey(id)

    init {
        uniqueIndex("idx_auth_credentials_user_auth_type", userId, authType)
    }
}