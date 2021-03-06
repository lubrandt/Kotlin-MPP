package de.lubrandt.kuestion.network

import io.ktor.client.HttpClient
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.*
import kotlin.browser.localStorage

actual val client = HttpClient {

    install(Auth) {
        basic {
            username = localStorage.getItem("user") ?: "peters"
            password = localStorage.getItem("password") ?: "lilie"
            realm = "Ktor Server"
            sendWithoutRequest = true
        }
    }

    install(Logging) {
        // logs to browser console
        logger = Logger.DEFAULT
        level = LogLevel.HEADERS
    }

    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}