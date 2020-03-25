package de.innosystec.kuestion

import io.ktor.application.*
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) = EngineMain.main(args)

internal fun Application.module() {
    val config = environment.config
    println("started")
}