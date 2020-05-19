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
    authenticate("basicAuth") {
        route("/endSurvey") {
            post {
                endSurvey(call.receive())
                call.respond(HttpStatusCode.OK)
            }
        }
    }


}