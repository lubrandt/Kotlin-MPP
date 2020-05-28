package de.innosystec.kuestion

import io.ktor.client.HttpClient
import io.ktor.client.features.*
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.AuthProvider
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.features.cookies.HttpCookies
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import io.ktor.util.encodeBase64
import io.ktor.utils.io.core.toByteArray

const val jvmBackend = "http://$jvmHost:$jvmPort"

val client = HttpClient {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.HEADERS
    }
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
    install(Auth) {
        basic {
            username = "username"
            password = "username"
            realm = "Ktor Server"
//            sendWithoutRequest = true
            //todo: auth header not being send after receiving a 401
        }
    }
    HttpResponseValidator {
        validateResponse {response: HttpResponse ->
            val statuscode = response.status.value
            when (statuscode) {
//                in 300..399 -> throw RedirectResponseException(response)
//                in 400..499 -> throw ClientRequestException(response)
//                in 500..599 -> throw ServerResponseException(response)
            }

            if (statuscode >= 600) {
                throw ResponseException(response)
            }

        }
        handleResponseException {cause: Throwable ->
            println("cause: $cause")

        }
    }
}

suspend fun getResultFromApi(id: String): SurveyReceiving {
    return client.get("${jvmBackend}/${id}")
}

suspend fun sendSurveyToApi(survey: SurveyCreation): String {
    return client.post("$jvmBackend/postSurvey") {
        contentType(ContentType.Application.Json)
        body = survey
    }
}

suspend fun sendClickedAnswerToApi(pair: ClickedAnswer): String {
    return client.post("$jvmBackend/inc") {
        contentType(ContentType.Application.Json)
        body = pair
    }
}

suspend fun getAllSurveys(): List<FrontSurvey> {
    return client.get("$jvmBackend/allSurveys")
}

suspend fun deleteSurvey(id: String) {
    return client.delete("$jvmBackend/$id")
}

suspend fun endSurvey(id:String) {
    return client.post("$jvmBackend/endSurvey") {
        contentType(ContentType.Application.Json)
//        header("Authorisation","username:username".encodeBase64()) // internalAPI....
        body = id
    }
}

