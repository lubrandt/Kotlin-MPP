package de.innosystec.kuestion

import de.innosystec.kuestion.handson.App
import de.innosystec.kuestion.spa.AppFrame
import react.dom.render
import kotlin.browser.document

fun main() {
    render(document.getElementById("root")) {
//        child(App::class) {}
//        child(AppFrame::class) {}
        child(Kuestion::class) {}
    }
}

