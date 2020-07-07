package de.innosystec.kuestion

import io.ktor.application.call
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.response.respondText
import io.ktor.routing.*

internal fun Routing.home() {
    route("/") {
        get {
            call.respondText("Hello Ktor GET Home")
        }
    }
    //todo: everything needed behind authzenticate {}
    // is this needed?
    static("/static") {
        resource("prod-dash.js")
    }
}