package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true  //本番ではデータサイズ減少のためfalse
            isLenient = false    //厳密でないJSONを許容しない
            ignoreUnknownKeys = true    //追加フィールドを無視（互換性のため）
            explicitNulls = true        //falseでnullフィールドをJSONから削除
        })
    }
}