package de.lubrandt.kuestion

import de.lubrandt.kuestion.exposed.dbAccessor
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.routing.route

internal fun Routing.endSurvey() {
    authenticate("basic") {
        route("/endSurvey") {
            post {
                val survey = call.receive<Int>()
                if (!dbAccessor.surveyExists(survey)) {
                    call.respond(HttpStatusCode.NotFound)
                } else {
                    dbAccessor.endSurvey(survey)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}