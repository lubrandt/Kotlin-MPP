package de.innosystec.kuestion

import de.innosystec.kuestion.charts.*
import de.innosystec.kuestion.spa.Home
import de.innosystec.kuestion.spa.Stuff
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.*
import kotlinx.css.*
import kotlinx.html.ButtonFormMethod
import kotlinx.html.ButtonType
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import react.router.dom.*
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
                    li {
                        navLink("/mockSu", exact = true) {
                            +"mockSurvey"
                        }
                    }
                    li {
                        navLink("/012345", exact = true) {
                            +"anotherSurvey"
                        }
                    }
                }
                div("content") {
                    route("/", CreateSurvey::class, exact = true)
                    route<IdProps>("/:id") { props ->
                        child(DisplaySurvey::class) {
                            attrs.id = props.match.params.id
                        }
                    }

                }
            }
        }

    }
}


interface KuestionState : RState {
    var response: String?
}