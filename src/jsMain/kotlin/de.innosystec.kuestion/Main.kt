package de.innosystec.kuestion

import react.dom.*
import kotlin.browser.document

const val jvmBackend = "http://$jvmHost:$jvmPort"

fun main() {
    render(document.getElementById("root")) {
        h1 {
            +"Hello, React+Kotlin/JS!"
        }
    }
}
