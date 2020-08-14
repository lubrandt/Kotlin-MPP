package de.lubrandt.kuestion

import de.lubrandt.kuestion.network.frontendAPI
import de.lubrandt.kuestion.utility.ComponentStyles
import de.lubrandt.kuestion.utility.scope
import kotlinx.coroutines.launch
import react.*
import react.dom.*
import react.router.dom.navLink
import styled.*

class SurveysList : RComponent<MainProps, SurveysListState>() {

    override fun SurveysListState.init() {
        surveys = emptyList()
    }

    private suspend fun updateListOfSurveys() {
        val resp = frontendAPI.getAllSurveys()
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

        styledDiv {
            css {
                +ComponentStyles.surveyList
            }
            styledDiv {
                css {
                    +ComponentStyles.surveyListLeftSide
                }
                div {
                    h3 {
                        +"Mocked Surveys "
                        br {}
                        +"(not working)"
                    }
                }
                div {
                    ul {
                        li {
                            navLink("${props.basepath}/mockSu/r", exact = true) {
                                +"Test Survey"
                            }
                        }
                        li {
                            navLink("${props.basepath}/0123456/r", exact = true) {
                                +"wrong SurveyID"
                            }
                        }
                    }
                }
            }

            styledDiv {
                css {
                    +ComponentStyles.surveyListRightSide
                }
                div {
                    h3 {
                        +"All created Surveys"
                    }
                }
                div {
                    ul {
                        state.surveys.forEach { item ->
                            li {
                                p {
                                    +item.question
                                }
                                navLink("${props.basepath}/${item.hash}/r", exact = true) {
                                    +"Display Survey"
                                }
                                navLink("${props.basepath}/${item.hash}/edit") {
                                    +"Edit Survey"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.surveysList(handler: MainProps.() -> Unit): ReactElement {
    return child(SurveysList::class) {
        this.attrs(handler)
    }
}

interface SurveysListState : RState {
    var surveys: List<HashQuestionPair>
}
