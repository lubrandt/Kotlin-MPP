package de.innosystec.kuestion

import de.innosystec.kuestion.network.getAllSurveys
import kotlinx.coroutines.launch
import react.*
import react.dom.*
import react.router.dom.navLink

class SurveysList : RComponent<RProps, SurveysListState>() {

    override fun SurveysListState.init() {
        surveys = emptyList()
    }

    private suspend fun updateListOfSurveys() {
        val resp = getAllSurveys()
        setState {
            surveys = resp
        }
    }

    override fun componentDidMount() {
        scope.launch {
            updateListOfSurveys()
        }
    }

    override fun RBuilder.render() {

        div {
            h3 {
                +"List of Mock Surveys"
            }
            ul {
                li {
                    navLink("/mockSu/r", exact = true) {
                        +"mockSurvey"
                    }
                }
                li {
                    navLink("/0123456/r", exact = true) {
                        +"tooLongSurvey"
                    }
                }
            }
        }

        div {
            h3 {
                +"List of all created Surveys"
            }
            ul {
                state.surveys.forEach { item ->
                    li {
                        navLink("/${item.hash}/r", exact = true) {
                            +item.question
                        }
                        navLink("/${item.hash}/edit") {
                            +"EditSurvey"
                        }
                    }
                }
            }
        }
    }
}

interface SurveysListState : RState {
    var surveys: List<FrontSurvey>
}
