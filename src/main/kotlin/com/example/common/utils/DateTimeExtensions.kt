package com.example.common.utils

import kotlinx.datetime.TimeZone
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime

private val JST = TimeZone.of("Asia/Tokyo")

fun Instant.conversionJst(): String =
    this.toLocalDateTime(JST)
        .toString()