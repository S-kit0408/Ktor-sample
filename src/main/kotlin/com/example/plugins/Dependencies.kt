package com.example.plugins

import com.example.controllers.UserController
import com.example.services.UserService
import com.example.repositories.UserRepositoryImpl


data class AppDependencies(
    val userController: UserController
)

fun createDependencies(): AppDependencies {
    val userRepository = UserRepositoryImpl()
    val userService = UserService(userRepository)
    val userController = UserController(userService)

    return AppDependencies(
        userController
    )
}