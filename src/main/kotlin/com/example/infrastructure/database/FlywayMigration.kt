package com.example.infrastructure.database

import com.example.infrastructure.config.DatabaseConfig
import org.flywaydb.core.Flyway

object FlywayMigration {

    // マイグレーション
    fun migrate(config: DatabaseConfig) {
        println("starting migration ...")

        val flyway = Flyway.configure()
            .dataSource(config.url, config.user, config.password)
            .locations("classpath:db/migration")
            .baselineOnMigrate(true)    // 既存DBを使用可能に
            .baselineVersion("0")
            .load()

        try {
            val result = flyway.migrate()

            println("Database migration completed successfully")
            println("Migrations excuted: ${result.migrationsExecuted}")
            println("Target Version: ${result.targetSchemaVersion}")

        } catch (e: Exception) {
            println("Error migrating database migration")
            throw e
        }
    }


    // 検証
    fun validate(config: DatabaseConfig) {
        println("starting validate ...")

        val flyway = Flyway.configure()
            .dataSource(config.url, config.user, config.password)
            .locations("classpath:db/migration")
            .load()

        try {
            flyway.validate()
            println("Migration validation successfully")

        } catch (e: Exception) {
            println("Error migrating validation")
            throw e
        }
    }


    // DB クリーン
    fun clean(config: DatabaseConfig) {
        println("starting clean...")

        val flyway = Flyway.configure()
            .dataSource(config.url, config.user, config.password)
            .cleanDisabled(false)   //clean有効か
            .load()

        flyway.clean()
        println("Database cleaned")
    }
}