package com.example

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json

/**
 * JSON Serialization設定
 */
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