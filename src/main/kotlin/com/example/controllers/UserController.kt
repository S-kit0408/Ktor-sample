package com.example.controllers

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.resources.*
import io.ktor.http.HttpStatusCode

import com.example.routes.Users
import com.example.services.UserService
import com.example.utils.respondResult

class UserController(
    private val userService: UserService
) {
    fun Route.registerUserRoutes() {

        get<Users.ById> { resource ->
            val result = userService.getUserById(resource.id)
            call.respondResult(result)
        }
    }

}
