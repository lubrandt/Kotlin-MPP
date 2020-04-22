package de.innosystec.kuestion

import de.innosystec.kuestion.charts.PieChart
import react.*
import react.dom.*
import styled.*

class DisplaySurvey : RComponent<IdProps, RState>() {

    override fun RBuilder.render() {
        h3 {
            +"DisplaySurvey"
        }
        div {
            p {
                +"The ID is: ${props.id}"
            }
        }
        styledDiv {
            css {
                +ComponentStyles.chartStyle
            }
            child(functionalComponent = PieChart, props = props)
        }
    }
}

/**
 * this method is used to call this Component like a usual ReactComponent ( p { } )
 */
fun RBuilder.displaySurvey(handler: IdProps.() -> Unit): ReactElement {
    return child(DisplaySurvey::class) {
        this.attrs(handler)
    }
}

