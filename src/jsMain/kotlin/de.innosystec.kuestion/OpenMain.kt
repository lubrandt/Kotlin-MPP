package de.innosystec.kuestion

import react.*
import react.dom.div
import react.dom.h1
import react.dom.p
import react.router.dom.RouteResultMatch

class OpenMain :RComponent<MainProps,RState>() {
    override fun RBuilder.render() {
        div {
            h1 {
                +"Kuestion First Blood"
            }
            p {
                +"You have received the test response: ${props.response}"
            }
            p {
                +"You are logged in: ${props.isLoggedIn}"
            }
        }
    }

}

fun RBuilder.openMain(handler: MainProps.() -> Unit): ReactElement {
    return child(OpenMain::class) {
        this.attrs(handler)
    }
}

interface MainProps:RProps {
    var response : String?
    var isLoggedIn: Boolean
    var basepath: String
}