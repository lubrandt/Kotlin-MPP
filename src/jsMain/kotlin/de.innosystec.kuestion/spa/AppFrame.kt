package de.innosystec.kuestion.spa

import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import react.*
import react.dom.*
import react.router.dom.*
import react.router.dom.switch
import styled.css
import styled.styledDiv

interface IdProps : RProps {
    var id: Int
}

class AppFrame : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        hashRouter {
           div {
                h1 {
                    +"Simple SPA"
                }
                ul("header") {
                    li {
                        navLink("/", exact = true) {
                            +"Home"
                        }
                    }
                    li {
                        navLink("/stuff") {
                            +"Stuff"
                        }
                    }
                    li {
                        navLink("/user/user4526") {
                            +"MockUser"
                        }
                    }

                }
                div("content") {
                    route("/", Home::class, exact = true)
                    route("/Stuff", Stuff::class)
                    route<IdProps>("/user/:id") { props ->
                        child(Stuff::class) {
                            attrs.id = props.match.params.id
                        }
                    }

                }
            }

        }
    }
}
