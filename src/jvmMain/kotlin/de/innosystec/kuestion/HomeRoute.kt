package de.innosystec.kuestion

import io.ktor.application.call
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route

internal fun Routing.home() {
    route("/") {
        get {
            call.respondText("Hello Ktor GET Home")
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