package de.innosystec.kuestion.network

import de.innosystec.kuestion.*
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

suspend fun getResultFromApi(id: String): SurveyReceiving {
    return client.get("$jvmBackend/${id}")
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
//todo: api in common, android app, sevrer to server, aufschreiben obs funktioniert
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

suspend fun changedSurvey(id: String, survey: SurveyCreation) {
    return client.post("$jvmBackend/$id/update") {
        contentType(ContentType.Application.Json)
        body = survey
    }
}

