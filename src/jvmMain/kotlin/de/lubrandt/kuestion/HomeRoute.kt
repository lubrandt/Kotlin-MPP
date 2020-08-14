package de.lubrandt.kuestion

import io.ktor.application.call
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route

internal fun Routing.home() {
    route("/") {
        get {
            call.respondText("Hello Ktor GET Home")
        }
    }
    static("/static") {
        resource("prod-dash.js")
    }
}
