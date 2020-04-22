package de.innosystec.kuestion

import de.innosystec.kuestion.charts.PieChart
import react.*
import react.dom.*
import styled.*

val DisplaySurveyFunctional = functionalComponent<IdProps> { props ->
    h3 {
        +"DisplaySurveyFunctionalComponent"
    }
    div {
        p {
            +"The ID is: ${props.id}"
        }
    }
    styledDiv {
        css {
            // browser throws warning about dynamic style creation, how to externalize?
            +ComponentStyles.chartStyle
        }
        child(functionalComponent = PieChart, props = props)
    }
}
