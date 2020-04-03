package de.innosystec.kuestion.SPA

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*

class AppFrame :RComponent<RProps,RState>() {
    override fun RBuilder.render() {
        div {
            h1 {
                +"Simple SPA"
            }
            ul("header") {
                li {
                    a("/") {
                        +"Home"
                    }
                }
                li {
                    a("/stuff") {
                        +"Stuff"
                    }
                }
                li {
                    a("/contact") {
                        +"Contact"
                    }
                }
            }
            div("content") {

            }
        }
    }

}