package com.example

import com.example.infrastructure.database.DatabaseFactory
import io.ktor.server.application.Application

// データベース初期化プラグイン
fun Application.configureDatabase() {
    DatabaseFactory.init()
}