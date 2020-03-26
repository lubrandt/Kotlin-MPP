package de.innosystec.kuestion

import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) = EngineMain.main(args)

internal fun Application.module() {
    val config = environment.config
    install(ContentNegotiation) {
//        serialization()
    }
    install(Routing)

    routing {
        home()
    }
}

internal fun Routing.home() {
    route("/") {
        get {
            call.respond("Hello Ktor")
        }
    }
}