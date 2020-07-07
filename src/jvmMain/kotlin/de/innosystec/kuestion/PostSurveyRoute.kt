package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.createSurvey
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.routing.route

internal fun Routing.postSurvey() {
    authenticate("basic") {
        route("/postSurvey") {
            post {
                call.respond(createSurvey(call.receive()))
            }
        }
    }
}


