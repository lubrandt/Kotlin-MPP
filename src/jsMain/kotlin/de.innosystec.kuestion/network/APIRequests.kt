package de.innosystec.kuestion.network

import de.innosystec.kuestion.*
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

suspend fun getResultFromApi(id: String): SurveyPackage {
    return client.get("$jvmBackend/${id}")
}

suspend fun sendSurveyToApi(survey: SurveyPackage): String {
    return client.post("$jvmBackend/postSurvey") {
        contentType(ContentType.Application.Json)
        body = survey
    }
}

suspend fun sendClickedAnswerToApi(pair: StringPair): String {
    return client.post("$jvmBackend/inc") {
        contentType(ContentType.Application.Json)
        body = pair
    }
}

suspend fun getAllSurveys(): List<StringPair> {
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

suspend fun changedSurvey(id: String, survey: SurveyPackage) {
    return client.post("$jvmBackend/$id/update") {
        contentType(ContentType.Application.Json)
        body = survey
    }
}

