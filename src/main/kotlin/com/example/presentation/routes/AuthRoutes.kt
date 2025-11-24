package com.example.presentation.routes

import com.example.presentation.controllers.AuthController
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.authRoutes() {
    val authController by inject<AuthController>()

    routing {
        route("/api/auth") {
            // 登録
            post("/register/password") {
                authController.registerWithPassword(call)
            }
            post("/register/google") {
                authController.registerWithGoogle(call)
            }

            // ログイン
            post("/login/password") {
                authController.loginWithPassword(call)
            }
            post("/login/google") {
                authController.loginWithGoogle(call)
            }

            // 認証方法削除
            delete("/credentials/{credentialId}") {
                authController.deleteAuthCredential(call)
            }
        }
    }
}