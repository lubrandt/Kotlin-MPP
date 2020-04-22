package de.innosystec.kuestion

import kotlinx.css.*
import styled.StyleSheet

object ComponentStyles : StyleSheet("ComponentStyle") {
    val chartStyle by css {
        height = 150.px
        width = 150.px
    }
}

/**
 * Global Stylesheet?
 * injectGlobal needs to be in render?
 */
//val styles = CSSBuilder().apply {
//    body {
//        margin(0.px)
//        padding(0.px)
//    }
//}
//
//StyledComponents.injectGlobal(styles.toString())