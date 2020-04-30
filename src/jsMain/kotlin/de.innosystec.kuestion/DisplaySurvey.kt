package de.innosystec.kuestion

import de.innosystec.kuestion.charts.PieChart
import de.innosystec.kuestion.charts.ReactPieChart
import kotlinx.coroutines.launch
import react.*
import react.dom.*
import styled.*

class DisplaySurvey : RComponent<IdProps, DisplaySurveyState>() {

    override fun DisplaySurveyState.init() {
        receivedSurvey = SurveyReceiving()
    }

    override fun componentDidMount() {
        scope.launch {
            val response = getResultFromApi(props.id)
            setState {
                receivedSurvey = response
            }
        }
    }

    override fun componentDidUpdate(prevProps: IdProps, prevState: DisplaySurveyState, snapshot: Any) {
        if (props.id != prevProps.id) {
            scope.launch {
                val response = getResultFromApi(props.id)
                setState {
                    receivedSurvey = response
                }
            }
        }
    }

    override fun RBuilder.render() {
        h3 {
            +"Displaying Survey for ID [${props.id}]:"
        }
        div {
            p {
                +"Might take some time to load (indicator pending)"
                br {}
                +"received Survey: "
                br {}
                +state.receivedSurvey.question
                br {}
                +"${state.receivedSurvey.answers}"
            }
        }
        styledDiv {
            css {
                +ComponentStyles.chartStyle
            }
            ReactPieChart {
                attrs.data = state.receivedSurvey.answers.toTypedArray()
            }
        }
    }
}


interface DisplaySurveyState : RState {
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

