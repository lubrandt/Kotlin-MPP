package de.innosystec.kuestion

import io.ktor.client.HttpClient
import io.ktor.client.request.get

//TODO review: nachdem lukas das gescheit gemacht hat diesen dilettanten dreck entfernen
interface API {
    suspend fun getResultFromApi(id: String): SurveyPackage
}

const val jvmBackend = "http://$jvmHost:$jvmPort"

class API2(private val client: HttpClient): API {
    override suspend fun getResultFromApi(id: String): SurveyPackage {
        return client.get("$jvmBackend/${id}")
    }
}