package com.example.presentation.routes

import com.example.presentation.controllers.UserController
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

//  ユーザールーティング
fun Route.userRoutes() {
    val userController by inject<UserController>()

    route("/api/users") {
        // ユーザー一覧取得
        get { userController.listUsers(call) }

        // ユーザー作成
        post { userController.createUser(call) }

        // ユーザー取得
        get("/{id}") { userController.getUser(call) }

        // ユーザー更新
        put("/{id}") { userController.updateUser(call) }

        // ユーザー削除
        delete("/{id}") { userController.deleteUser(call) }
    }
}