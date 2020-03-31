package de.innosystec.kuestion

import io.ktor.client.HttpClient
import kotlinx.coroutines.await
import react.dom.*
import react.*
import kotlin.browser.document
import kotlinx.css.*
import styled.*
import kotlin.browser.window

const val jvmBackend = "http://$jvmHost:$jvmPort"

fun main() {
    render(document.getElementById("root")) {
//        child(App::class) {}
        child(Kuestion::class) {}
    }
}

