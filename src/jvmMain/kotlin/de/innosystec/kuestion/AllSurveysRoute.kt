package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.dbAccessor
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route

internal  fun Routing.allSurveys() {
    authenticate("basic") {
        route("/allSurveys") {
            get {
                call.respond(dbAccessor.getAllSurveys())
            }
        }
    }
}
