package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.includeSurveyChanges
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

internal fun Routing.updateSurvey() {
    authenticate("basic") {
        route("/{questionId}/update") {
            post {
                val changes = call.receive<SurveyPackage>() // modified version of survey
                val hash = call.parameters["questionId"]
                if (hash == null) {
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    includeSurveyChanges(hash, changes)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}