package de.innosystec.kuestion

import de.innosystec.kuestion.network.client
import de.innosystec.kuestion.network.jvmBackend
import de.innosystec.kuestion.utility.ComponentStyles
import de.innosystec.kuestion.utility.styles
import io.ktor.client.request.get
import kotlinx.coroutines.*
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import react.router.dom.*
import styled.*
import kotlin.browser.localStorage

class Kuestion : RComponent<RProps, KuestionState>() {

    override fun KuestionState.init() {
        val mainScope = MainScope()
        mainScope.launch {
            val getResponse = client.get<String>("$jvmBackend/") {
//                header("Authorization","Basic bHVrYXM6bHVrYXM=") //Lukas:Lukas
            }
            setState {
                response = getResponse
            }
        }
        isLoggedIn = false
    }

    fun RBuilder.checkElementLoginStatus(element: ReactElement): ReactElement {
        return if (checkLoginStatus()) {
            element
        } else {
            redirect("", "/login") // path weitergeben? match.path? route has props -> history?
        }
    }


    override fun RBuilder.render() {
        StyledComponents.injectGlobal(styles.toString())
        // hashrouter vs browserrouter???
        // https://stackoverflow.com/questions/51974369/hashrouter-vs-browserrouter
        hashRouter {
            div {
                styledDiv {
                    h1 {
                        +"Kuestion"
                    }
                    p {
                        +"A Survey Tool"
                    }
                    css {
                        +ComponentStyles.headline
                    }
                }
                styledDiv {
                    css {
                        +ComponentStyles.navbar
                    }
                    ul {
                        li {
                            navLink("/", exact = true) {
                                +"Home"
                            }
                        }
                        li {
                            navLink("/surveys") {
                                +"Survey Stuff"
                            }
                        }
                        li {
                            navLink("/login") {
                                +"Login"
                            }
                        }
                        li {
                            a {
                                +"Logout"
                                attrs.onClickFunction = {
                                    localStorage.clear()
                                    setState {} // force update
                                }
                            }
                        }
                    }
                }
                styledDiv {
                    css {
                        +ComponentStyles.loggedIn
                    }
                    p {
                        +if (checkLoginStatus()) "Logged in as ${localStorage.getItem("user")}" else "Logged out"
                    }
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
                            checkElementLoginStatus(
                                protectedMain {
                                    basepath = "/surveys"
                                }
                            )
                        }
                        route("") {
                            h1 { +"Page not found" }
                        }
                    }
                }
            }
        }
    }
}

fun checkLoginStatus(): Boolean {
    val user = localStorage.getItem("user")
    val password = localStorage.getItem("password")
    return if (user.isNullOrEmpty() || password.isNullOrEmpty()) {
        false
    } else user == password
}

interface KuestionState : RState {
    var response: String?
    var isLoggedIn: Boolean
    var user: String
}

interface IdProps : RProps {
    var id: String

}


