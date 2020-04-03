package de.innosystec.kuestion

import io.ktor.application.*
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.features.statusFile
import io.ktor.html.respondHtml
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
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

    install(StatusPages) {
        statusFile(HttpStatusCode.NotFound, filePattern = "error#.html")
    }

    install(Routing)

    routing {
        home()
        route("/survey_not_found") {
            get {
                call.respond("Sorry, this survey does not exist")
            }
        }
        static("/static") {
            resource("prod-dash.js")
        }
        questions()
    }
}

internal fun Routing.home() {
    route("/") {
        get {
            call.respondText("Hello Ktor Home")
        }
    }
}

internal fun Routing.questions() {
    route("/{questionId}") {
        // create unique id for every question, store in db
        get {
            val hash = call.parameters["questionId"]
            call.respond("questionID:$hash")
        }
    }
    route("/{questionId}/results") {
        // fetch results for questionId
        get {
            call.respond("questionID/Results?")
        }
    }
}