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
            sendWithoutRequest = true // todo: update ktor to 1.3.5 and set to false?
        }
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.HEADERS
    }
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }

//    HttpResponseValidator {
//        validateResponse {response: HttpResponse ->
//            val statuscode = response.status.value
//            when (statuscode) {
////                in 300..399 -> throw RedirectResponseException(response)
////                in 400..499 -> throw ClientRequestException(response)
////                in 500..599 -> throw ServerResponseException(response)
//            }
//
//            if (statuscode >= 600) {
//                throw ResponseException(response)
//            }
//
//        }
//        handleResponseException {cause: Throwable ->
//            println("cause: $cause")
//
//        }
//    }
}