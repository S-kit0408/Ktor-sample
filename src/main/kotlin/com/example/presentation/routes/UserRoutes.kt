package com.example.presentation.routes

import com.example.presentation.controllers.UserController
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

//  ユーザールーティング
fun Route.userRoutes() {
    val userController by inject<UserController>()

    authenticate("clerk-jwt") {
        route("/api/users") {
            // ユーザー一覧取得
            get { userController.listUsers(call) }

            // ユーザー作成（通常はClerk経由なので不要だが、残す場合）
            post { userController.createUser(call) }

            // ユーザー取得
            get("/{id}") { userController.getUser(call) }

            // ユーザー更新
            put("/{id}") { userController.updateUser(call) }

            // ユーザー削除
            delete("/{id}") { userController.deleteUser(call) }
        }
    }
}