package de.innosystec.kuestion

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.response.respondText
import io.ktor.routing.*

internal fun Routing.home() {
    //todo: is authenticate here necessary? öhm... nö?
    authenticate("basic") {
        route("/") {
            get {
                call.respondText("Hello Ktor GET Home")
            }
        }
    }
    //todo: is this needed? vmtl ja
    static("/static") {
        resource("prod-dash.js")
    }
}