package com.example.infrastructure.config

// データベース設定
data class DatabaseConfig(
    val url: String,
    val driver: String,
    val user: String,
    val password: String,
    val maxPoolSize: Int = 10
) {
    companion object {
        fun default(): DatabaseConfig {
            return DatabaseConfig(
                url = EnvConfig.get("DB_URL", "jdbc:postgresql://localhost:5432/ktor_db"),
                driver = "org.postgresql.Driver",
                user = EnvConfig.get("DB_USER", "postgres"),
                password = EnvConfig.get("DB_PASSWORD", "postgres"),
                maxPoolSize = EnvConfig.get("DB_POOL_SIZE", "10").toIntOrNull() ?: 10
            )
        }

        fun fromEnvironment(): DatabaseConfig {
            return DatabaseConfig(
                url = EnvConfig.require("DB_URL"),
                driver = "org.postgresql.Driver",
                user = EnvConfig.require("DB_USER"),
                password = EnvConfig.require("DB_PASSWORD"),
                maxPoolSize = EnvConfig.get("DB_POOL_SIZE", "10").toIntOrNull() ?: 10
            )
        }
    }
}