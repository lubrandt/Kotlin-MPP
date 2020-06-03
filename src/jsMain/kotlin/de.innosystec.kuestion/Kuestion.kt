package de.innosystec.kuestion

import de.innosystec.kuestion.network.client
import de.innosystec.kuestion.network.jvmBackend
import io.ktor.client.request.get
import kotlinx.coroutines.*
import react.*
import react.dom.*
import react.router.dom.*

class Kuestion : RComponent<RProps, KuestionState>() {

    override fun KuestionState.init() {
        val mainScope = MainScope()
        mainScope.launch {
            val getResponse = client.get<String>("$jvmBackend/")
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
                        navLink("/allSurveys", exact = true) {
                            +"Display all Created surveys"
                        }
                    }
                }

                div("content") {
                    //login //autoredirect
                    route("/", CreateSurvey::class, exact = true)
                    route("/allSurveys", SurveysList::class, exact = true)
                    route<IdProps>("/:id/edit") { props ->
                        updateSurvey {
                            id = props.match.params.id
                        }
                    }
                    route<IdProps>("/:id/r") { props ->
                        displaySurvey {
                            id = props.match.params.id
                        }
                    }
                    //redirect("","") todo: prio: auth, editierung todo: Antwort hat ja schon survey hash, kein survey hash schicken, chartslicedata umbauen
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


