package de.innosystec.kuestion

import react.dom.render
import kotlin.browser.document

fun main() {
    render(document.getElementById("root")) {
        child(Kuestion::class) {}
    }
}



