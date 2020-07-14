package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.includeSurveyChanges
import de.innosystec.kuestion.exposed.surveyExists
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
                // modified version of survey, when no correct json is send, server throws 500
                val changes: SurveyPackage? = call.receive()

                val hash: String? = call.parameters["questionId"]

                if (hash == null || changes == null) {
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    if (includeSurveyChanges(hash, changes) && surveyExists(hash)) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
            }
        }
    }
}