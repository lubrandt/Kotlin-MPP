package de.innosystec.kuestion

import react.*
import react.dom.*
import react.router.dom.navLink
import react.router.dom.route
import react.router.dom.switch

class ProtectedMainPage : RComponent<MainProps, RState>() {
    override fun RBuilder.render() {
        h1 {
            +"Survey Overview"
            br {}
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
                    errorBoundary {
                        child = displaySurvey {
                            id = props.match.params.id
                        }
                        childList.add(
                            displaySurvey {
                                id = props.match.params.id
                            }
                        )
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