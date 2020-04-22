package de.innosystec.kuestion

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get

const val jvmBackend = "http://$jvmHost:$jvmPort"

val client = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

suspend fun getResultFromApi(id: String): Array<ChartSliceData> {
    return client.get("${jvmBackend}/${id}")
}

suspend fun sendSurveyToApi() {}