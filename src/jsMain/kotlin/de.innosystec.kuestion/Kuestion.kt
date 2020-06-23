package de.innosystec.kuestion

import de.innosystec.kuestion.network.client
import de.innosystec.kuestion.network.jvmBackend
import de.innosystec.kuestion.utility.Auth
import io.ktor.client.request.get
import kotlinx.coroutines.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import react.router.dom.*
import kotlin.browser.localStorage

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

    fun RBuilder.checkElementLoginStatus(element: ReactElement): ReactElement {
        return if (checkLoginStatus()) {
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
                div {
                    button {
                        +"Logout"
                        attrs.onClickFunction = {
                            localStorage.clear()
                            setState{} // force update
                        }

                    }
                }
                p {
                    +"You are logged in: ${checkLoginStatus()}"
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
                    //redirect("","") todo: prio: auth, editierung todo: Antwort hat ja schon survey hash, kein survey hash schicken, chartslicedata umbauen
                }
            }
        }
    }
}

fun checkLoginStatus(): Boolean {
    val user = localStorage.getItem("user")
    val password = localStorage.getItem(("password"))
    return if (user.isNullOrEmpty()){
        println("user: $user <- null or empty")
        false
    } else if (password.isNullOrEmpty()){
        println("pw: $password <- null or empty")
        false
    } else if (user == password) {
        println("$user == $password, logged in")
        true
    } else {
        println("$user != $password")
        false
    }
}

interface KuestionState : RState {
    var response: String?
    var isLoggedIn: Boolean
    var user: String
}

interface IdProps : RProps {
    var id: String

}


