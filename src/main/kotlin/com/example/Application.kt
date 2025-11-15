package com.example

import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain

import com.example.plugins.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val dependencies = createDependencies()

    configureSerialization()
    configureCORS()
    configureRouting(dependencies)
}