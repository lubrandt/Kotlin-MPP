package de.innosystec.kuestion

import de.innosystec.kuestion.utility.ComponentStyles
import react.*
import react.dom.*
import react.router.dom.navLink
import react.router.dom.route
import react.router.dom.switch
import styled.*

class ProtectedMainPage : RComponent<MainProps, RState>() {
    override fun RBuilder.render() {
        div {
            styledDiv {
                css {
                    +ComponentStyles.navbar
                }
                div {
                    h2 {
                        +"Survey Overview"
                    }
                }
                div {
                    ul {
                        li {
                            navLink("${props.basepath}/allSurveys") {
                                +"Display all Created surveys"
                            }
                        }
                        li {
                            navLink("${props.basepath}/createSurveys") {
                                +"Create a Survey"
                            }
                        }
                    }
                }
            }
            div {
                switch {
                    route<MainProps>("${props.basepath}/allSurveys") {
                        surveysList {
                            basepath = props.basepath
                        }
                    }
                    route<MainProps>("${props.basepath}/createSurveys") {
                        createSurvey {
                            basepath = props.basepath
                        }
                    }
                    route<IdProps>("${props.basepath}/:id/edit") { props ->
                        updateSurvey {
                            id = props.match.params.id

                        }
                    }
                    route<IdProps>("${props.basepath}/:id/r") { props ->
                        displaySurvey {
                            id = props.match.params.id
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.protectedMain(handler: MainProps.() -> Unit): ReactElement {
    return child(ProtectedMainPage::class) {
        this.attrs(handler)
    }
}