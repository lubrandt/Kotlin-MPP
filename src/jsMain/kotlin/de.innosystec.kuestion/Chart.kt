package de.innosystec.kuestion

import de.innosystec.kuestion.charts.PieChart
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.coroutines.*
import react.*
import react.dom.*

class Chart : RComponent<IdProps, ChartState>() {
    companion object : RStatics<IdProps, ChartState, Chart, Nothing>(Chart::class) {
        init {
            var result = arrayOf<ChartSliceData>()
            getDerivedStateFromProps = { nextProps, prevState ->
                if (nextProps.id !== prevState.prevID) {
                    println("if")
                    CoroutineScope(Dispatchers.Default).launch {
                        result = getResultFromApi(nextProps.id)
                    }
                    ChartState(result, nextProps.id)
                } else {
                    println("else")
                    // return null to indicate no change
                    null
                }
            }
        }
    }

    init {
        CoroutineScope(Dispatchers.Default).launch {
            val response = getResultFromApi(props.id)
            setState {
                dataGraph = response
            }
        }
        println("ChartInit")
    }

    override fun RBuilder.render() {
        p {
            +"Hello Chart!"
        }
        p {
            +"your id is: ${props.id}"
            +"Your GraphData is: ${state.dataGraph}"
        }
        PieChart {
            attrs.data = state.dataGraph
        }
    }

}

private val client = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

suspend fun getResultFromApi(id: String): Array<ChartSliceData> {
    return client.get<Array<ChartSliceData>>("${jvmBackend}/${id}")
}


interface ChartProps : RProps {
//    var dataGraph: Array<ChartSliceData>
}

data class ChartState(var dataGraph: Array<ChartSliceData>, var prevID: String) : RState {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class.js != other::class.js) return false

        other as ChartState

        if (!dataGraph.contentEquals(other.dataGraph)) return false

        return true
    }

    override fun hashCode(): Int {
        return dataGraph.contentHashCode()
    }
}


fun RBuilder.chart(handler: IdProps.() -> Unit): ReactElement {
    return child(Chart::class) {
        this.attrs(handler)
    }
}