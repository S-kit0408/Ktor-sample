package com.example.infrastructure.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

// Usersテーブル定義
object UsersTable : Table("users") {
    val id = varchar("id", 26)
    val clerkUserId = varchar("clerk_user_id", 255).uniqueIndex()
    val email = varchar("email", 255).uniqueIndex()
    val name = varchar("name", 100)
    val avatarUrl = varchar("avatar_url", 500).nullable()
    val primaryAuthProvider = varchar("primary_auth_provider", 20).default("unknown")
    val defaultPrivacySetting = varchar("default_privacy_setting", 20).default("private")
    val lastLoginAt = timestamp("last_login_at").nullable()
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")

    override val primaryKey = PrimaryKey(id)
}