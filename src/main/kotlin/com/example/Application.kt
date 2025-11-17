package com.example

import com.example.infrastructure.database.DatabaseFactory
import com.example.infrastructure.config.EnvConfig
import com.example.presentation.routes.userRoutes
import io.ktor.server.netty.EngineMain
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger


fun main(args: Array<String>) {
    EngineMain.main(args)
}

@Suppress("unused")
fun Application.module() {

    // koin di
    install(Koin) {
        slf4jLogger()
        modules(
            domainModule,
            applicationModule,
            infrastructureModule,
            presentationModule
        )
    }

    // plugin
    configureSerialization()

    // テスト時のみ（インメモリ）
//     configureDatabase()

    // アプリケーション終了時のクリーンアップ
//    environment.monitor.subscribe(ApplicationStopped) {
//        DatabaseFactory.close()
//    }

    // routing
    routing {
        get("/") {
            call.respondText("HELLO WORLD!")
        }

        // user routes
        userRoutes()
    }
}