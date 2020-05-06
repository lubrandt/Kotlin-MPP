package de.innosystec.kuestion

import kotlinx.coroutines.launch
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import react.router.dom.navLink

class SurveysList : RComponent<RProps, SurveysListState>() {

    override fun SurveysListState.init() {
        surveys = emptyList()
    }

    private suspend fun updateListOfSurveys() {
        val resp = getAllSurveys()
        setState{
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
                state.surveys.forEach {item ->
                    li {
                        navLink("/${item.hash}/r", exact = true) {
                            +item.question
                        }
                        button {
                            +"Delete Survey"
                            attrs.onClickFunction = {
                                scope.launch {
                                    deleteSurvey(item.hash)
                                    updateListOfSurveys()
                                }
                            }
                        }
                        button {
                            +"Edit Survey"
                        }
                        button {
                            +"End Survey"
                            //todo: actual expected time with common code(color beispiel)??
                            //todo: basic-auth with name == pw
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
