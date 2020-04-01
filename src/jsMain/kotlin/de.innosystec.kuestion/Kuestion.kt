package de.innosystec.kuestion

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.*
import react.dom.*

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
        div {
            PieChart {
                attrs.data = dataMock
            }
        }
    }
}


interface KuestionState: RState {
    var response:String?
}