package de.innosystec.kuestion

import kotlinx.coroutines.launch
import react.*
import react.dom.*
import react.router.dom.navLink

class SurveysList : RComponent<RProps, SurveysListState>() {

    override fun SurveysListState.init() {
        surveys = emptyList()
    }

    override fun componentDidMount() {
        scope.launch {
            val resp = getAllSurveys()
            setState{
                surveys = resp
            }
        }
    }

    override fun RBuilder.render() {

        h3 {
            +"List of all Surveys"
        }
        ul("header") {

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
            state.surveys.forEach {
                li {
                    navLink("/${it.hash}/r", exact = true) {
                        +it.question
                    }
                }
            }
        }
    }

}

interface SurveysListState : RState {
    var surveys: List<FrontSurvey>
}
