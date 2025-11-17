package com.example.infrastructure.config

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv

// 環境変数管理 .env.local → .env → システム環境変数
object EnvConfig {
    private val dotenv: Dotenv by lazy {
        dotenv {
            filename = ".env"
            ignoreIfMissing = true
        }
    }

    fun get(key: String, defaultValue: String = ""): String {
        // 1. システム環境変数をチェック
        System.getenv(key)?.let { return it }

        // 2. .env.localをチェック
        dotenv[key]?.let { return it }

        // 3. .envをチェック
        val fallbackDotenv = try {
            dotenv {
                filename = ".env.local"
                ignoreIfMissing = true
            }
        } catch (e: Exception) {
            null
        }
        fallbackDotenv?.get(key)?.let { return it }

        return defaultValue
    }

    // 環境変数を取得 見つからない場合は例外をスロー
    fun require(key: String): String {
        return get(key).ifEmpty {
            throw IllegalStateException("Required environment variable '$key' is not set")
        }
    }
}