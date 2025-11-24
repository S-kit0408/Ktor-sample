package com.example.common.utils

import com.example.domain.services.EmailAlreadyExistsException
import com.example.presentation.models.responses.ErrorResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

suspend fun ErrorHandle(call: ApplicationCall, exception: Throwable) {
    when (exception) {
        is EmailAlreadyExistsException -> {
            call.respond(
                HttpStatusCode.Conflict,
                ErrorResponse(
                    error = "CONFLICT",
                    message = exception.message ?: "Email already exists"
                )
            )
        }
        is IllegalArgumentException -> {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    error = "BAD_REQUEST",
                    message = exception.message ?: "Invalid input"
                )
            )
        }
        is NoSuchElementException -> {
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(
                    error = "NOT_FOUND",
                    message = exception.message ?: "Resource not found"
                )
            )
        }
        else -> {
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    error = "INTERNAL_SERVER_ERROR",
                    message = "An unexpected error occurred"
                )
            )
        }
    }
}