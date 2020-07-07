package de.innosystec.kuestion

import de.innosystec.kuestion.utility.ComponentStyles
import de.innosystec.kuestion.utility.styles
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import react.router.dom.*
import styled.StyledComponents
import styled.css
import styled.injectGlobal
import styled.styledDiv
import kotlin.browser.localStorage

class Kuestion : RComponent<RProps, KuestionState>() {

    override fun KuestionState.init() {
        isLoggedIn = false
    }

    private fun RBuilder.checkElementLoginStatus(element: ReactElement): ReactElement {
        return if (checkLoginStatus()) {
            element
        } else {
            redirect("", "/login")
        }
    }


    override fun RBuilder.render() {
        StyledComponents.injectGlobal(styles.toString())

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
                    switch {
                        route("/", exact = true) {
                            openMain {
                                isLoggedIn = state.isLoggedIn
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
                        route<MainProps>("/surveys") {
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
    var isLoggedIn: Boolean
}

interface IdProps : RProps {
    var id: String
}


