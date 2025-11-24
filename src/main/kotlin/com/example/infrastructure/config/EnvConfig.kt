package com.example.infrastructure.config

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv

object EnvConfig {
    private val dotenv: Dotenv by lazy {
        dotenv {
            filename = ".env.local"
            ignoreIfMissing = true
        }
    }

    fun get(key: String, defaultValue: String = ""): String {
        System.getenv(key)?.let { return it }

        // .env.localをチェック
        dotenv[key]?.let { return it }

        // .envをチェック
        val fallbackDotenv = try {
            dotenv {
                filename = ".env"
                ignoreIfMissing = true
            }
        } catch (e: Exception) {
            null
        }
        fallbackDotenv?.get(key)?.let { return it }

        return defaultValue
    }

    // 環境変数取得　なければ例外をスロー
    fun require(key: String): String {
        return get(key).ifEmpty {
            throw IllegalStateException("Required environment variable '$key' is not set")
        }
    }
}