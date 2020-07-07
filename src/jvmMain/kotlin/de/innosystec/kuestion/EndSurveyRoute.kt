package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.endSurvey
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
                // remove the quotation signs from start and end
                val survey = call.receive<String>().drop(1).dropLast(1)
                if (survey.length != 6) {
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    endSurvey(survey)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}