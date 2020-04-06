package de.innosystec.kuestion.spa

import react.*
import react.dom.*

class Stuff :RComponent<IdProps,RState>() {
    override fun RBuilder.render() {
        div {
            h2 {
                +"STUFF"
            }
            p {
                +"Duis a turpis sed lacus dapibus elementum sed eu lectus."
            }
            ol {
                li {
                    +"Nulla pulvinar diam + ${props.id}"
                }
                li {
                    +"Facilisis bibendum"
                }
            }
        }
    }
}