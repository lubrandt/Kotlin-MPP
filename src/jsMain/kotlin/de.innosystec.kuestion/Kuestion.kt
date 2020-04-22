package de.innosystec.kuestion

import io.ktor.client.request.get
import kotlinx.coroutines.*
import react.*
import react.dom.*
import react.router.dom.*

class Kuestion : RComponent<IdProps, KuestionState>() {

    override fun KuestionState.init() {
        val mainScope = MainScope()
        mainScope.launch {
            val getResponse = client.get<String>("${jvmBackend}/")
            setState {
                response = getResponse
            }
        }
    }

    override fun RBuilder.render() {
        hashRouter {
            div {
                h1 {
                    +"Kuestion First Blood"
                }
                p {
                    +"You have received the test response: ${state.response}"
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
                        navLink("/0123456", exact = true) {
                            +"tooLongSurvey"
                        }
                    }
                }
                div("content") {
                    route("/", CreateSurvey::class, exact = true)
                    route<IdProps>("/:id") { props ->
                        displaySurvey {
                            id = props.match.params.id
                        }
//                        child(functionalComponent = DisplaySurveyFunctional, props = props.match.params)
                    }
                }
            }
        }
    }
}

interface KuestionState : RState {
    var response: String?
}

interface IdProps : RProps {
    var id: String
}


