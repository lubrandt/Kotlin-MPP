package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.addAnswerCount
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

internal fun Routing.counter() {
    authenticate("basic") {
        route("/inc") {
            post {
                //todo: move time check to frontend? reduce data bandwith?
                addAnswerCount(call.receive())
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}