package de.innosystec.kuestion

import de.innosystec.kuestion.network.getAllSurveys
import de.innosystec.kuestion.utility.ComponentStyles
import de.innosystec.kuestion.utility.scope
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
                                    +item.first
                                }
                                navLink("${props.basepath}/${item.second}/r", exact = true) {
                                    +"Display Survey"
                                }
                                navLink("${props.basepath}/${item.second}/edit") {
                                    +"Edit Survey"
                                }
                            }
                        }
                    }
                }//todo: visual feedback for buttons
                //todo: react bootsrtrap?
                //https://react-bootstrap.github.io/
                //https://getbootstrap.com/docs/4.1/getting-started/introduction/
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
    var surveys: List<StringPair>
}
