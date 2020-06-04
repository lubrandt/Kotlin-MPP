package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.includeSurveyChanges
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

/**
 * Update Survey, eg Edits, stop Survey
 */
internal fun Routing.updateSurvey() {
//    authenticate("basicAuth") {
        route("/{questionId}/update") {
            post {
                val changes = call.receive<SurveyPackage>() // modified version of survey
                val hash = call.parameters["questionId"]
//                println("THIS IS WHAT I GOT: $hash, $changes")
                if (hash == null)  {
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    includeSurveyChanges(hash, changes)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
//    }
}