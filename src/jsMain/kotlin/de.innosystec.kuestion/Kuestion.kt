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
        isLoggedIn = false
    }

    fun RBuilder.checkLoginStatus(element: ReactElement): ReactElement {
        return if (state.isLoggedIn) {
            element
        } else {
            redirect("", "/login") // path weitergeben? match.path? route has props -> history?
        }
    }

    override fun RBuilder.render() {
        // hashrouter vs browserrouter???
        // https://stackoverflow.com/questions/51974369/hashrouter-vs-browserrouter
        hashRouter {

            div {
                h3 { +"NavBar" }
                ul("header") {
                    li {
                        navLink("/", exact = true) {
                            +"Home"
                        }
                    }
                    li {
                        navLink("/login") {
                            +"Login"
                        }
                    }
                    li {
                        navLink("/surveys") {
                            +"Survey Stuff"
                        }
                    }
                }
                p {
                    +"You are logged in: ${state.isLoggedIn}"
                }

                div("content") {
                    // move protected routes to protected route with own hashrouter?
                    switch {
                        route("/", exact = true) {
                            openMain {
                                isLoggedIn = state.isLoggedIn
                                response = state.response
                            }
                        }
                        route("/login") {
                            loginPage {
                                loginFunction = { input ->
                                    setState {
                                        isLoggedIn = input
                                    }
                                }
                            }
                        }
                        route<MainProps>("/surveys") { props ->
                            checkLoginStatus(protectedMain {basepath = "/surveys" })
                        }
                        route("") {
                            h1 { +"not found" }
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
    var isLoggedIn: Boolean
}

interface IdProps : RProps {
    var id: String
}


