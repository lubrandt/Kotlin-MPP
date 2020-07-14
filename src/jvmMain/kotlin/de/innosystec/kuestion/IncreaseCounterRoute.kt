package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.addAnswerCount
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

internal fun Routing.counter() {
    authenticate("basic") {
        route("/inc") {
            post {
                val content: StringPair? = call.receiveOrNull()
                if (content == null) {
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    addAnswerCount(content)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}
