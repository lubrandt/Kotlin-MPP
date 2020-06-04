package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.*
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.route

internal fun Routing.getSurvey() {
    route("/{questionId}") {

        get {
            val hash = call.parameters["questionId"]
            if (hash != null) {
                if (hash.length != 6) {
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    if (surveyExists(hash)) {
                        val data = SurveyPackage()
                        data.answers = getAnswers(hash).toMutableList()
                        data.question = getQuestion(hash)
                        data.expirationTime = getExpirationTime(hash)
                        call.respond(data)
                    } else {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        delete {
            val hash = call.parameters["questionId"]
            if (hash == null) {
                call.respond(HttpStatusCode.BadRequest)
            } else {
                deleteSurvey(hash)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

