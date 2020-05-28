package de.innosystec.kuestion

import react.dom.render
import kotlin.browser.document
import kotlin.js.Date

fun main() {
    render(document.getElementById("root")) {
        child(Kuestion::class) {}
    }
}

val dateOfToday = Date().toISOString().substring(0, 10)

