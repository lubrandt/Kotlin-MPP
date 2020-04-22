package de.innosystec.kuestion

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get

val client = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

suspend fun getResultFromApi(id: String): Array<ChartSliceData> {
    return client.get("${jvmBackend}/${id}")
}