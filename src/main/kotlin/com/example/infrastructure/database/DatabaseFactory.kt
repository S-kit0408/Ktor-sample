package com.example.infrastructure.database

import com.example.infrastructure.config.DatabaseConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction



// データベース接続とトランザクション管理
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

        println("Connected to database")

        // Flywayマイグレーション実行
        FlywayMigration.migrate(config)
    }

    fun close() {
        if (::dataSource.isInitialized && !dataSource.isClosed) {
            println("closing database connection...")
            dataSource.close()
            println("closed database connection")
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}