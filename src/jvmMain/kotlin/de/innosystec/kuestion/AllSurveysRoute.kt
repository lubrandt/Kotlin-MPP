package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.getAllCreatedSurveys
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route

internal  fun Routing.allSurveys() {
    route("/allSurveys") {
        get {
            call.respond(getAllCreatedSurveys())
        }
    }
}
