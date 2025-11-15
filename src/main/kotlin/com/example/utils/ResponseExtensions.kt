package com.example.utils

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.*
import io.ktor.server.application.ApplicationCall
import com.example.models.dto.ApiResponse


suspend inline fun <reified T> ApplicationCall.respondResult(
    result: Result<T>,
    successStatus: HttpStatusCode = HttpStatusCode.OK
) {
    result
        .onSuccess { data ->
            respond(successStatus, ApiResponse(
                success = true,
                data = data,
            ))
        }
        .onFailure { error  ->
            val (status, message) = when (error) {
                is NoSuchElementException -> HttpStatusCode.NotFound to (error.message ?: "リソースが見つかりません")
                is IllegalArgumentException -> HttpStatusCode.BadRequest to (error.message ?: "不正なリクエストです")
                is IllegalStateException -> HttpStatusCode.NotFound to (error.message ?: "競合が発生しました")
                else -> {
                    application.environment.log.error("Unexpected error", error)
                    HttpStatusCode.InternalServerError to "内部サーバーエラーが発生しました"
                }
            }
            respond(status, ApiResponse<T>(
                success = false,
                message = message
            ))
        }
}