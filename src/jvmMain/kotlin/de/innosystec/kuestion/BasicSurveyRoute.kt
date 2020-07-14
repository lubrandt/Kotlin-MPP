package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.*
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.locations.*
import io.ktor.response.respond
import io.ktor.routing.*

// not a "real" class
@KtorExperimentalLocationsAPI
@Location("/{questionId}")
class question(val questionId: String?)

@KtorExperimentalLocationsAPI
internal fun Routing.getSurvey() {
    authenticate("basic") {
        get<question> { question ->
            val hash = question.questionId
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

        delete<question> { question ->
            val hash = question.questionId
            if (hash == null || !surveyExists(hash)) {
                call.respond(HttpStatusCode.BadRequest)
            } else {
                deleteSurvey(hash)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

