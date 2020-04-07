package de.innosystec.kuestion

import de.innosystec.kuestion.charts.PieChart
import de.innosystec.kuestion.charts.dataMock
import kotlinx.css.*
import react.*
import react.dom.p
import styled.css
import styled.styledDiv

class DisplaySurvey :RComponent<IdProps,RState>() {
    override fun RBuilder.render() {
        // if survey not found, redirect to home/404?
        styledDiv {
            p {
                +"your id is: ${props.id}"
            }
            css {
                height = 150.px
                width = 150.px
            }
            PieChart {
                attrs.data = dataMock
            }
        }
    }
}
interface IdProps : RProps {
    var id: Int
}
