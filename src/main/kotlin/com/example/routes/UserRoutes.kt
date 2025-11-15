package com.example.routes

import io.ktor.resources.*
import kotlinx.serialization.Serializable


@Resource("/api/users")
@Serializable
class Users {

    @Resource("{id}")
    @Serializable
    data class ById(val parent: Users = Users(), val id: Int)
}


