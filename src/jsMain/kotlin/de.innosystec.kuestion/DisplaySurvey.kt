package de.innosystec.kuestion

import de.innosystec.kuestion.charts.PieChart
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinext.js.asJsObject
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.css.*
import react.*
import react.dom.p
import styled.css
import styled.styledDiv
import kotlin.browser.window

class DisplaySurvey : RComponent<IdProps, SurveyState>() {

    override fun SurveyState.init() {
        val mainScope = MainScope()
        mainScope.launch {
            val response = fetchResponse(props.id)
            setState {
                graphData = response
            }
        }
    }

    override fun RBuilder.render() {
        // if survey not found, redirect to home/404?

        styledDiv {
            css {
                height = 150.px
                width = 150.px
            }
            p {
                +"your id is: ${props.id}"
            }
            PieChart {
               attrs.data = state.graphData
            }
            p {
                +"MockPieChart:"
            }
            PieChart {
                attrs.data = dataMock.toTypedArray()
            }
        }
    }
}

interface IdProps : RProps {
    var id: String
}

interface SurveyState: RState {
    var graphData: Array<ChartSliceData>
}

suspend fun fetchResponse(id: String): Array<ChartSliceData> =
    window.fetch("${jvmBackend}/${id}").await().json().await().unsafeCast<Array<ChartSliceData>>()

