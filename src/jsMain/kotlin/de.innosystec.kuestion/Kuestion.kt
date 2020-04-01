package de.innosystec.kuestion

import de.innosystec.kuestion.charts.*
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.*
import kotlinx.css.*
import react.*
import react.dom.*
import styled.*

class Kuestion : RComponent<RProps, KuestionState>() {

    private val client = HttpClient()

    override fun KuestionState.init() {
        val mainScope = MainScope()
        mainScope.launch {
            val getResponse = client.get<String>("${jvmBackend}/blood")
            setState {
                response = getResponse
            }
        }
    }
    override fun RBuilder.render() {
        h3 {
            +"Kuestion First Blood"
        }
        div {
            +"You have received: ${state.response}"
        }
        styledDiv {
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


interface KuestionState: RState {
    var response:String?
}