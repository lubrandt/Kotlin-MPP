package de.innosystec.kuestion.network

import de.innosystec.kuestion.*
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

const val jvmBackend = "http://$jvmHost:$jvmPort"

class ServerAPIImpl(private val client: HttpClient) : ServerAPI {
    override suspend fun getSurveyFromApi(id: Int): SurveyPackage {
        return client.get("$jvmBackend/${id}")
    }

    override suspend fun sendSurveyToApi(survey: SurveyPackage): Int {
        return client.post("$jvmBackend/postSurvey") {
            contentType(ContentType.Application.Json)
            body = survey
        }
    }

    override suspend fun sendClickedAnswerToApi(answer: SurveyAnswerPair) {
        return client.post("$jvmBackend/inc") {
            contentType(ContentType.Application.Json)
            body = answer
        }
    }

    override suspend fun getAllSurveys(): List<HashQuestionPair> {
        return client.get("$jvmBackend/allSurveys")
    }

    override suspend fun deleteSurvey(id: Int) {
        return client.delete("$jvmBackend/$id")
    }

    override suspend fun endSurvey(id: Int) {
        return client.post("$jvmBackend/endSurvey") {
            contentType(ContentType.Application.Json)
            body = id
        }
    }

    override suspend fun sendChangedSurvey(id: Int, survey: SurveyPackage) {
        return client.post("$jvmBackend/$id/update") {
            contentType(ContentType.Application.Json)
            body = survey
        }
    }
}

