package de.innosystec.kuestion

import de.innosystec.kuestion.charts.PieChart
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.DIV
import kotlinx.html.id
import react.*
import react.dom.p
import styled.StyledDOMBuilder
import styled.css
import styled.styledDiv

class DisplaySurvey : RComponent<IdProps, SurveyState>() {

    init {
//        var response = arrayOf<ChartSliceData>()
//        val coScope = GlobalScope.launch {
//            response = client.get<Array<ChartSliceData>>("${jvmBackend}/${props.id}")
//            setState {
//                isLoaded = true
//                dataGraph = response
//            }
//        }
        println("DisplayInit")
    }

    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

//    override fun SurveyState.init() {
//        var response = arrayOf<ChartSliceData>()
//        val coScope = GlobalScope.launch {
//            response = client.get<Array<ChartSliceData>>("${jvmBackend}/${props.id}")
//            setState {
////                isLoaded = true
//                dataGraph = response
//            }
//        }
//
//    }

    override fun RBuilder.render() {
        // if survey not found, redirect to home/404?


        styledDiv {
            css {
                height = 150.px
                width = 150.px
            }
            if (!state.isLoaded) {
                p {
                    +"loading..."
                }
            }

            p {
                +"your id is: ${props.id}\n"
                +"Your GraphData is: ${state.dataGraph}"
            }
            chart {
                attrs.id = props.id
            }
            p {
                +"below chart"
            }
        }


//            p {
//                +"MockPieChart:"
//            }
//            PieChart {
//                attrs.data = dataMock.toTypedArray()
//            }
    }
}


interface IdProps : RProps {
    var id: String
//    var onIdChange: (String) -> Unit

}

interface SurveyState : RState {
    var dataGraph: Array<ChartSliceData>
    var isLoaded: Boolean
}




