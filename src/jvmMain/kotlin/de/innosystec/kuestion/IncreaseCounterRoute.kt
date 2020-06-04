package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.addAnswerCount
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

internal fun Routing.counter() {
    route("/inc") {
        post {
            val incAnswer = call.receive<StringPair>()
            addAnswerCount(incAnswer)
            call.respond(HttpStatusCode.OK)
        }
    }
}