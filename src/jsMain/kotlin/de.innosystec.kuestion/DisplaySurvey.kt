package de.innosystec.kuestion

import de.innosystec.kuestion.network.getResultFromApi
import de.innosystec.kuestion.network.sendClickedAnswerToApi
import de.innosystec.kuestion.utility.ReactPieChart
import de.innosystec.kuestion.utility.ComponentStyles
import de.innosystec.kuestion.utility.createChartSliceArray
import de.innosystec.kuestion.utility.scope
import kotlinx.coroutines.launch
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import styled.*

class DisplaySurvey : RComponent<IdProps, DisplaySurveyState>() {

    override fun DisplaySurveyState.init() {
        receivedSurvey = SurveyPackage()
    }

    private suspend fun updateSurvey() {
        val response = getResultFromApi(props.id)
        setState {
            receivedSurvey = response
        }
    }

    override fun componentDidMount() {
        scope.launch {
            updateSurvey()
        }
    }


    override fun componentDidUpdate(prevProps: IdProps, prevState: DisplaySurveyState, snapshot: Any) {
        if (props.id != prevProps.id) {
            scope.launch {
                updateSurvey()
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
                +"Question: ${state.receivedSurvey.question}"
                br {}
                +"List of Answers: ${state.receivedSurvey.answers}"
                br {}
                +"ExpirationDate: ${state.receivedSurvey.expirationTime}"
            }
        }
        styledDiv {
            css {
                +ComponentStyles.chartStyle
            }
            ReactPieChart {
                attrs.data = createChartSliceArray(state.receivedSurvey.answers)
            }
        }
        ul {
            state.receivedSurvey.answers.forEach { item ->
                li {
                    +"[${item.counts} Stimme(n)] ${item.text}"
                    attrs.onClickFunction = {
                        scope.launch {
                            sendClickedAnswerToApi(
                                StringPair(
                                    props.id,
                                    item.text
                                )
                            )
                            updateSurvey()
                        }
                    }
                }
            }
        }
    }
}

interface DisplaySurveyState : RState {
    var receivedSurvey: SurveyPackage
}

/**
 * this method is used to call this Component like a usual Component ( displaySurvey { } )
 */
fun RBuilder.displaySurvey(handler: IdProps.() -> Unit): ReactElement {
    return child(DisplaySurvey::class) {
        this.attrs(handler)
    }
}


