package de.lubrandt.kuestion

import de.lubrandt.kuestion.exposed.*
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.locations.*
import io.ktor.response.respond
import io.ktor.routing.*

// not a "real" class
@KtorExperimentalLocationsAPI
@Location("/{questionId}")
class question(val questionId: String)

@KtorExperimentalLocationsAPI
internal fun Routing.getSurvey() {
    authenticate("basic") {
        get<question> { question ->
            val hash = question.questionId.toIntOrNull()
            if (hash != null) {
                if (dbAccessor.surveyExists(hash)) {
                    call.respond(
                        SurveyPackage(
                            dbAccessor.getQuestion(hash),
                            dbAccessor.getAnswers(hash).toMutableList(),
                            dbAccessor.getExpirationTime(hash)
                        )
                    )
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        delete<question> { question ->
            val hash = question.questionId.toIntOrNull()
            if (hash == null || !dbAccessor.surveyExists(hash)) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                dbAccessor.deleteSurvey(hash)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

