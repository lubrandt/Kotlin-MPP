package de.innosystec.kuestion

import de.innosystec.kuestion.charts.PieChart
import io.ktor.client.request.get
import kotlinx.coroutines.*
import kotlinx.css.*
import react.*
import react.dom.*
import styled.*

class DisplaySurvey : RComponent<IdProps, SurveyState>() {
    companion object : RStatics<IdProps, SurveyState, DisplaySurvey, Nothing>(DisplaySurvey::class) {
        init {
            var result = arrayOf<ChartSliceData>()
            getDerivedStateFromProps = { nextProps, prevState ->
                if (nextProps.id !== prevState.prevID) {
                    println("if")
                    CoroutineScope(Dispatchers.Default).launch {
                        result = getResultFromApi(nextProps.id)
                    }
                    SurveyState(result, nextProps.id)
                } else {
                    println("else")
                    // return null to indicate no change
                    null
                }
            }
        }
    }

    override fun componentDidMount() {
        CoroutineScope(Dispatchers.Default).launch {
            val response = getResultFromApi(props.id)
            setState {
                dataGraph = response
            }
        }
    }

    override fun RBuilder.render() {
        p {
            +"Hello Chart!"
        }
        p {
            +"your id is: ${props.id}"
            +"Your GraphData is: ${state.dataGraph}"
        }
        styledDiv {
            css {
                height = 150.px
                width = 150.px
            }
            PieChart {
                attrs.data = state.dataGraph
            }
        }
    }
}

suspend fun getResultFromApi(id: String): Array<ChartSliceData> {
    return client.get("${jvmBackend}/${id}")
}

data class SurveyState(var dataGraph: Array<ChartSliceData>, var prevID: String) : RState {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class.js != other::class.js) return false

        other as SurveyState

        if (!dataGraph.contentEquals(other.dataGraph)) return false

        return true
    }

    override fun hashCode(): Int {
        return dataGraph.contentHashCode()
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

