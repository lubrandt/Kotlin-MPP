package de.innosystec.kuestion

import react.*

class DisplaySurvey :RComponent<IdProps,RState>() {
    override fun RBuilder.render() {
        // if survey not found, redirect to home/404?
    }

}
interface IdProps : RProps {
    var id: Int
}
