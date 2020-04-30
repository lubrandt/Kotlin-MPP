package de.innosystec.kuestion

import de.innosystec.kuestion.charts.PieChart
import kotlinx.coroutines.launch
import react.*
import react.dom.*
import styled.*

class DisplaySurvey : RComponent<IdProps, DisplaySurveyState>() {

    override fun DisplaySurveyState.init() {
        scope.launch {
            receivedSurvey = getResultFromApi(props.id)
        }
    }

    override fun RBuilder.render() {
        h3 {
            +"Displaying Survey for ID [${props.id}]:"
        }
        div {
            p {
                +"Might take some time to load, indicator pending"
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

interface DisplaySurveyState:RState {
    var receivedSurvey: SurveyReceiving
    var changedSurvey: SurveyCreation // send updated survey
}

/**
 * this method is used to call this Component like a usual Component ( displaySurvey { } )
 */
fun RBuilder.displaySurvey(handler: IdProps.() -> Unit): ReactElement {
    return child(DisplaySurvey::class) {
        this.attrs(handler)
    }
}

