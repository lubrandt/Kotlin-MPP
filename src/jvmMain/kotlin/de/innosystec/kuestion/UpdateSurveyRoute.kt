package de.innosystec.kuestion

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

/**
 * Update Survey, eg Edits, stop Survey
 */
internal fun Routing.updateSurvey() {
    authenticate("basicAuth") {
        route("/{questionId}/update") {
            post {
                val changes = call.receive<SurveyCreation>()
                val hash = call.parameters["questionId"]
                call.respond(HttpStatusCode.OK)
            }
            get {
                //todo: todo what?
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}