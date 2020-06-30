package de.innosystec.kuestion.utility

import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import kotlinx.css.properties.TextDecoration
import styled.StyleSheet

object ComponentStyles : StyleSheet("ComponentStyle") {
    val chartStyle by css {
        height = 150.px
        width = 150.px
    }
    val headline by css {
        fontFamily = "Verdana"
    }
    val navbar by css {
        display = Display.flex
        flexDirection = FlexDirection.row
        justifyContent = JustifyContent.center
        children("li") {
            display = Display.inline
            listStyleType = ListStyleType.none
            margin(0.px)
            backgroundColor = Color.antiqueWhite
            children("a") {
                fontWeight = FontWeight.w700
                textDecoration = TextDecoration.none
                padding(20.px)
                display = Display.inlineBlock
                color = Color.black
                cursor = Cursor.pointer
                borderStyle = BorderStyle.solid
                borderWidth = 1.px
            }
        }
    }
}

/**
 * Global Stylesheet
 */
val styles = CSSBuilder().apply {
    body {
//        margin(10.px)
//        padding(20.px)
//        width = 700.px
        fontFamily = "Helvetica"
        textAlign = TextAlign.center
    }

}

