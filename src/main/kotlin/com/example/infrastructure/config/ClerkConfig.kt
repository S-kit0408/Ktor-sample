package com.example.infrastructure.config

object ClerkConfig {
    val clerkSecretKey: String = EnvConfig.require("CLERK_SECRET_KEY")
    val clerkPublishableKey: String = EnvConfig.require("CLERK_PUBLISHABLE_KEY")
    val clerkDomain: String = EnvConfig.require("CLERK_DOMAIN") // 例: "your-app.clerk.accounts.dev"
}