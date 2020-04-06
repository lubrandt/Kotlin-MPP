package de.innosystec.kuestion.spa

import react.*
import react.dom.*

class Home : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        div {
            h2 {
                +"HELLO"
            }
            p {
                +"Cras facilisis urna ornare ex volutpat, et convallis erat elementum. Ut aliquam, ipsum vitae gravida suscipit, metus dui bibendum est, eget rhoncus nibh"
            }
            p{
                +"Duis a turpis sed lacus dapibus elementum sed eu lectus."
            }
        }
    }

}