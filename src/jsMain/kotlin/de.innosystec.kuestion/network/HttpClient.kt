package de.innosystec.kuestion.network

import de.innosystec.kuestion.jvmHost
import de.innosystec.kuestion.jvmPort
import io.ktor.client.HttpClient
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.*
import kotlin.browser.localStorage

const val jvmBackend = "http://$jvmHost:$jvmPort"

val client = HttpClient {

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