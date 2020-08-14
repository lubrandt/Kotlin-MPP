package de.lubrandt.kuestion.utility

import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import styled.StyleSheet

/**
 * Global Stylesheet
 */
val styles = CSSBuilder().apply {
    body {
        display = Display.flex
        flexDirection = FlexDirection.row
        justifyContent = JustifyContent.center
        fontFamily = "Helvetica"
        textAlign = TextAlign.center
    }
    button {
        backgroundColor = rgb(151, 151, 151)
        padding(10.px, 25.px)
        margin(5.px)
        textAlign = TextAlign.center
        textDecoration = TextDecoration.none
        display = Display.inlineBlock
        cursor = Cursor.pointer
        fontSize = 16.px
        borderStyle = BorderStyle.solid
        borderWidth = 1.px
        borderColor = Color.black
        fontWeight = FontWeight.w700
        color = Color.black
    }

}

/**
 * Individual css instructions per val
 */
object ComponentStyles : StyleSheet("ComponentStyle") {
    val chartStyle by css {
        height = 150.px
        width = 150.px
    }

    val headline by css {
        fontFamily = "Verdana"
        h1 {
            marginBottom = 3.px
        }
        p {
            marginTop = 5.px
            marginBottom = 50.px
        }
    }

    val openMain by css {
        width = 400.px
        margin(LinearDimension.auto)
        children("p") {
            textAlign = TextAlign.left
        }
    }

    val navbar by css {
        ul {
            width = 400.px
            margin(LinearDimension.auto)
            fontWeight = FontWeight.w700
            listStyleType = ListStyleType.none
            padding(0.px)
            overflow = Overflow.hidden
            backgroundColor = rgb(151, 151, 151)
            children("li") {
                float = Float.left
                children("a") {
                    display = Display.block
                    textAlign = TextAlign.center
                    padding(14.px, 16.px)
                    color = Color.black
                    textDecoration = TextDecoration.none
                    hover {
                        backgroundColor = rgb(120, 120, 120)
                    }
                }
            }
        }
    }

    val loginPage by css {
        boxSizing = BoxSizing.borderBox
        input {
            padding(12.px, 12.px)
            borderRadius = 4.px
        }
        label {
            padding(12.px, 12.px, 12.px, 0.px)
            display = Display.inlineBlock
        }
    }

    val loggedIn by css {
        margin(LinearDimension.auto)
        width = 560.px
        children("p") {
            textAlign = TextAlign.right
        }
    }

    val surveyList by css {
        width = 550.px
        margin(LinearDimension.auto)
        ul {
            listStyleType = ListStyleType.none
            padding(0.px)
            overflow = Overflow.hidden
            children("li") {
                a {
                    textAlign = TextAlign.center
                    display = Display.inlineBlock
                    padding(14.px, 16.px)
                    margin(5.px)
                    fontWeight = FontWeight.w100
                    color = Color.black
                    backgroundColor = rgb(151, 151, 151)
                    textDecoration = TextDecoration.none
                    hover {
                        backgroundColor = rgb(120, 120, 120)
                    }
                }
            }
        }
    }

    val surveyListLeftSide by css {
        float = Float.left
        width = 50.pct
    }

    val surveyListRightSide by css {
        float = Float.right
        width = 50.pct
    }

    val displaySurveyInfo by css {
        display = Display.flex
        flexWrap = FlexWrap.wrap
        width = 50.pct
        margin(LinearDimension.auto)
    }

    val Surveyinfo by css {
        textAlign = TextAlign.left
        flex(flexBasis = 100.pct)
    }

    val buttons by css {
        margin(15.px)
    }

    val flex100pct by css {
        flex(flexBasis = 100.pct)
    }

    val inputFields by css {
        input {
            padding(12.px, 12.px)
            borderRadius = 4.px
            float = Float.left
        }
    }
}



