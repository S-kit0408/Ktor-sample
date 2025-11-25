package com.example.presentation.routes

import com.example.presentation.controllers.AuthController
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.authRoutes() {
    val authController by inject<AuthController>()

    authenticate("clerk-jwt") {
        route("/api/auth") {
            // ユーザー情報同期
            post("/sync") { authController.syncUser(call) }

            // 現在のユーザー情報取得
            get("/me") { authController.getCurrentUser(call) }
        }
    }
}