package de.innosystec.kuestion

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.response.respondText
import io.ktor.routing.*

internal fun Routing.home() {
    route("/") {
        authenticate("basic") {
            get {
                call.respondText("Hello Ktor GET Home")
            }
        }
        post {
            // create survey here
            call.respondText("Hello Ktor POST Home")
        }
    }
    // is this needed?
    static("/static") {
        resource("prod-dash.js")
    }
}