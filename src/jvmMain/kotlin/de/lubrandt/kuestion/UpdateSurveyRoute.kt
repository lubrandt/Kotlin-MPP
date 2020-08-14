package de.lubrandt.kuestion

import de.lubrandt.kuestion.exposed.dbAccessor
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
                val hash: Int? = call.parameters["questionId"]?.toInt()

                if (hash == null || changes == null) {
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    dbAccessor.includeSurveyChanges(hash, changes)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}