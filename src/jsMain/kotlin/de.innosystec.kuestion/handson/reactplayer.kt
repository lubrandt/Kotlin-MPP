@file:JsModule("react-player")
@file:JsNonModule

package de.innosystec.kuestion.handson

import react.*

@JsName("default")
external val ReactPlayer: RClass<ReactPlayerProps>

external interface ReactPlayerProps: RProps {
    var url: String
}

