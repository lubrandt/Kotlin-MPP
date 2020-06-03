package de.innosystec.kuestion.network

import de.innosystec.kuestion.jvmHost
import de.innosystec.kuestion.jvmPort
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.*

const val jvmBackend = "http://$jvmHost:$jvmPort"

val client = HttpClient {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.HEADERS
    }
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
//    install(Auth) {
//        basic {
//            username = "username"
//            password = "username"
//            realm = "Ktor Server"
////            sendWithoutRequest = true
//            //todo: auth header not being send after receiving a 401
//        }
//    }
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