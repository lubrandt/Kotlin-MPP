package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.endSurvey
import io.ktor.application.call
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*

internal fun Routing.endSurvey() {
//    authenticate("basicAuth") {
        route("/endSurvey") {
            post {
                val survey = call.receive<String>().drop(1).dropLast(1) // remove the " from start and end
                if (survey.length != 6) {
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    endSurvey(survey)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
//    }
}