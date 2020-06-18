package de.innosystec.kuestion

import de.innosystec.kuestion.utility.buttonLink
import kotlinx.html.js.onClickFunction
import kotlinx.html.onClick
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import react.router.dom.navLink
import react.router.dom.redirect

class LoginPage : RComponent<LoginProps, RState>() {

    override fun RBuilder.render() {
        div {
            h1 {
                +"Welcome to the LoginPage"
            }
        }
        div {
            button {
                navLink("/surveys") { +"Login" }
                attrs.onClickFunction = {
                    props.loginFunction(true)
                }
            }
            br {}
            button {
                navLink("/") { +"Logout" }
                attrs.onClickFunction = { props.loginFunction(false) }
            }
        }
    }

}

interface LoginProps : RProps {
    var loginFunction: (Boolean) -> Unit
    var path: String
}

fun RBuilder.loginPage(handler: LoginProps.() -> Unit): ReactElement {
    return child(LoginPage::class) {
        this.attrs(handler)
    }
}

// landingState mit loginstatus? alle anderen Seiten als Kinder? childList?
