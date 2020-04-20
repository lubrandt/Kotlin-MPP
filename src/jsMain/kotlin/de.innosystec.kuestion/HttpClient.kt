package de.innosystec.kuestion

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer

val client = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}