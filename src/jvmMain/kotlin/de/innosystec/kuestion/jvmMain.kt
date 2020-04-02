package de.innosystec.kuestion

import io.ktor.application.*
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.html.respondHtml
import io.ktor.http.HttpMethod
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.json
import io.ktor.serialization.serialization
import io.ktor.server.netty.EngineMain
import kotlinx.html.body
import kotlinx.html.h1

fun main(args: Array<String>) = EngineMain.main(args)


internal fun Application.module() {
//    val config = environment.config
    install(ContentNegotiation) {
        json()
    }
    install(CORS) {
        method(HttpMethod.Get)
        anyHost()
        allowCredentials = true
    }
    install(Routing)

    routing {
        home()
        route("/blood") {
            get {
                call.respond("Good Day Sir")
            }
        }
        route("/{questionId}") {
            // create unique id for every question, store in db
            get {
                val hash = call.parameters["questionId"]
                call.respond("questionID?")
            }
        }
        route("/{questionId}/results") {
            // fetch results for questionId
            get {
                call.respond("questionID/Results?")
            }
        }
        static("/static") {
            resource("prod-dash.js")
        }
    }
}

internal fun Routing.home() {
    route("/") {
        get {
            call.respondText("Hello Ktor Home")
//            call.respondHtml {
//                body {
//                    h1 { "Hello from KTOR" }
//                }
//            }

        }
    }
}