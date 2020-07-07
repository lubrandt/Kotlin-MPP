package de.innosystec.kuestion

import de.innosystec.kuestion.network.getResultFromApi
import de.innosystec.kuestion.network.sendClickedAnswerToApi
import de.innosystec.kuestion.utility.*
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import styled.*
import kotlin.js.Date

class DisplaySurvey : RComponent<IdProps, DisplaySurveyState>() {

    override fun DisplaySurveyState.init() {
        receivedSurvey = SurveyPackage()
        hasError = false
    }

    private suspend fun updateSurvey() {
        val response: SurveyPackage
        //todo: ErrorBoundary?
        try {
            response = getResultFromApi(props.id)
            setState {
                receivedSurvey = response
            }

        } catch (e: Exception) {
            println("Oh no! Something happend: $e")
            setState {
                hasError = true
            }
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
        if (state.hasError) {
            h1 {
                +"This Survey doesn't exist! (DisplaySurveyComponent)"
            }
            p { +"something went wrong..." }
        } else {
            div {
                h4 {
                    +"Question: ${state.receivedSurvey.question}"
                }
                p {
                    +"ExpirationDate: ${state.receivedSurvey.expirationTime}"
                }
                p {
                    +"Time:"
                }
            }
            styledDiv {
                css {
                    display = Display.flex
                    flexDirection = FlexDirection.row
                    width = 80.pct
                }
                styledDiv {
                    css {
                        +ComponentStyles.chartStyle
                        flex(flexBasis = 50.pct)
                    }
                    //todo: Standardfarben? ~200?
                    ReactPieChart {
                        attrs.data = createChartSliceArray(state.receivedSurvey.answers)
                    }
                }
                styledDiv {
                    css {
                        textAlign = TextAlign.left
                        flex(flexBasis = 50.pct)
                    }
                    ul {
                        state.receivedSurvey.answers.forEach { item ->
                            li {
                                +"[${item.counts} Stimme(n)] ${item.text}"
                                attrs.onClickFunction = {
                                    // commonMain Usecase
                                    if (Zeiten.checkDate(Zeit(state.receivedSurvey.expirationTime))) {
                                        scope.launch {
                                            sendClickedAnswerToApi(
                                                StringPair(
                                                    props.id,
                                                    item.text
                                                )
                                            )
                                            //todo: feedback wenn abgelaufen ?
                                            updateSurvey()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

interface DisplaySurveyState : RState {
    var receivedSurvey: SurveyPackage
    var hasError: Boolean
}

/**
 * this method is used to call this Component like a usual Component ( displaySurvey { } )
 */
fun RBuilder.displaySurvey(handler: IdProps.() -> Unit): ReactElement {
    return child(DisplaySurvey::class) {
        this.attrs(handler)
    }
}


