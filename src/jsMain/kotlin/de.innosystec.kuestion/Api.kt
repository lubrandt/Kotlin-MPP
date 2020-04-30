package de.innosystec.kuestion

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType

const val jvmBackend = "http://$jvmHost:$jvmPort"

val client = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

suspend fun getResultFromApi(id: String): SurveyReceiving {
    return client.get("${jvmBackend}/${id}")
}

suspend fun sendSurveyToApi(survey: SurveyCreation): String {
    return client.post<String>("$jvmBackend/postSurvey") {
        contentType(ContentType.Application.Json)
        body = survey
    }
}