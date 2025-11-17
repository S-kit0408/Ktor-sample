package com.example.infrastructure.database

import com.example.infrastructure.config.DatabaseConfig
import com.example.infrastructure.database.tables.UsersTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * データベース接続とトランザクション管理
 */
object DatabaseFactory {
    private lateinit var dataSource: HikariDataSource

    fun init(config: DatabaseConfig = DatabaseConfig.default()) {
        // HikariCPコネクションプールの設定
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = config.url
            driverClassName = config.driver
            username = config.user
            password = config.password
            maximumPoolSize = config.maxPoolSize

            // PostgreSQL用の推奨設定
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"

            // コネクションプールの最適化
            validate()
        }

        dataSource = HikariDataSource(hikariConfig)

        // データベース接続
        val database = Database.connect(dataSource)

        // テーブル作成（本番環境ではマイグレーションツールを使用することを推奨）
        transaction(database) {
            SchemaUtils.create(
                UsersTable,
            )
        }
    }

    fun close() {
        if (::dataSource.isInitialized && !dataSource.isClosed) {
            dataSource.close()
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}