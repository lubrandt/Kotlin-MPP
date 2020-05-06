package de.innosystec.kuestion

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.routing.*

/**
 * Update Survey, eg Edits, stop Survey
 */
internal fun Routing.updateSurvey() {
    authenticate("basicAuth") {
        route("/{questionId}/update") {
            post {
                val changes = call.receive<SurveyCreation>() // SurveyCreation has no expire date field...
                val hash = call.parameters["questionId"]

            }
        }
    }
}