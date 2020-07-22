package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.dbAccessor
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.*

internal fun Routing.counter() {
    authenticate("basic") {
        route("/inc") {
            post {
                val content: SurveyAnswerPair? = call.receiveOrNull()
                if (content == null || !dbAccessor.surveyExists(content.survey)) {
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    dbAccessor.addAnswerCount(content)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}
