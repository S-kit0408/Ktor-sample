package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(dependencies: AppDependencies) {
    install(Resources)

    routing {
        get("/") {
            call.respondText { "Hello World!" }
        }

        with(dependencies.userController) {
            registerUserRoutes()
        }
    }
}