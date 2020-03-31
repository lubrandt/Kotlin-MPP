package de.innosystec.kuestion

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.*
import react.dom.div
import react.dom.h1

class Kuestion : RComponent<RProps, KuestionState>() {

    val client = HttpClient()

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
        h1 {
            +"Kuestion First Blood"
        }
        div {
            +"You have received: ${state.response}"
        }
    }

}


interface KuestionState: RState {
    var response:String?
}