package de.innosystec.kuestion

import de.innosystec.kuestion.utility.ComponentStyles
import react.*
import react.dom.*
import styled.*

class OpenMain : RComponent<MainProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                +ComponentStyles.openMain
            }
            h1 {
                +"LandingPage"
            }
            p {
                +"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, "
                +"sed diam nonumy eirmod tempor invidunt ut labore et dolore "
                +"magna aliquyam erat, sed diam voluptua. At vero eos et accusam "
                +"et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea "
                +"takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor "
                +"sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor "
                +"invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. "
                +"At vero eos et accusam et justo duo dolores et ea rebum. Stet clita "
                +"kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."
            }
        }
    }

}

fun RBuilder.openMain(handler: MainProps.() -> Unit): ReactElement {
    return child(OpenMain::class) {
        this.attrs(handler)
    }
}

interface MainProps : RProps {
    var isLoggedIn: Boolean
    var basepath: String
}