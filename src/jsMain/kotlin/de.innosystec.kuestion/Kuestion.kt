package de.innosystec.kuestion

import de.innosystec.kuestion.charts.*
import de.innosystec.kuestion.spa.Home
import de.innosystec.kuestion.spa.Stuff
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.*
import kotlinx.css.*
import react.*
import react.dom.*
import react.router.dom.hashRouter
import react.router.dom.navLink
import react.router.dom.route
import styled.*

class Kuestion : RComponent<IdProps, KuestionState>() {

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
        hashRouter {
            div {
                h3 {
                    +"Kuestion First Blood"
                }
                p {
                    +"You have received: ${state.response}"
                }
                ul("header") {
                    li {
                        navLink("/", exact = true) {
                            +"HomeCreateApp"
                        }
                    }
                }
                div("content") {
                    route("/", Creation::class, exact = true)
                    route<IdProps>("/:questionId") { props ->
                        child(DisplaySurvey::class) {
                            attrs.id = props.match.params.id
                        }
                    }
                }
            }
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