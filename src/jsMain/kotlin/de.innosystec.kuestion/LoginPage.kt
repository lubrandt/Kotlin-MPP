package de.innosystec.kuestion

import de.innosystec.kuestion.utility.ComponentStyles
import kotlinext.js.jsObject
import kotlinx.css.Color
import kotlinx.css.backgroundColor
import kotlinx.css.color
import kotlinx.html.*
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import react.router.dom.navLink
import react.router.dom.redirect
import styled.*
import kotlin.browser.localStorage

class LoginPage : RComponent<LoginProps, LoginState>() {

    override fun componentDidMount() {
        setState {
            user = localStorage.getItem("user") ?: ""
            password = ""
        }
    }

    private fun handleFormSubmit() {
        localStorage.setItem("user", state.user)
        localStorage.setItem("password", state.password)
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                +ComponentStyles.loginPage
                children("input") {
                    backgroundColor = Color.bisque
                }
            }
            styledDiv {
                h2 {
                    +"Please enter a valid username & password"
                }
                styledDiv {
                    css {
                        color = Color.red
                    }
                    if (!checkLoginStatus()) p { +"wrong password or username" } else redirect("/login", "/surveys")
                }
            }
            // https://github.com/JetBrains/kotlin-wrappers/issues/35
            styledDiv {
                css {
                    +ComponentStyles.loginPage
                }

                form {
                    attrs.onSubmitFunction = { handleFormSubmit() }
                    div {
                        div {
                            label {
                                +"Username"
                            }
                        }
                        div {
                            input(InputType.text) {
                                attrs.name = "user"
                                attrs.value = state.user
                                attrs.onChangeFunction = { event ->
                                    val newValue = (event.target as HTMLInputElement).value
                                    setState { user = newValue }
                                }
                            }
                        }
                    }
                    br {}
                    div {
                        div {
                            label {
                                +"Password"
                            }
                        }
                        div {
                            input(InputType.password) {
                                attrs.name = "password"
                                attrs.value = state.password
                                attrs.onChangeFunction = { event ->
                                    val newValue = (event.target as HTMLInputElement).value
                                    setState { password = newValue }
                                }
                            }
                        }
                    }
                    br {}
                    br {}
                    div {
                        button(type = ButtonType.submit) {
                            +"Sign In"
                        }
                    }
                }
            }
        }
    }
}

interface LoginProps : RProps {
    var loginFunction: (Boolean) -> Unit
    var path: String
}

interface LoginState : RState {
    var user: String
    var password: String
}

fun RBuilder.loginPage(handler: LoginProps.() -> Unit): ReactElement {
    return child(LoginPage::class) {
        this.attrs(handler)
    }
}

// landingState mit loginstatus? alle anderen Seiten als Kinder? childList?
