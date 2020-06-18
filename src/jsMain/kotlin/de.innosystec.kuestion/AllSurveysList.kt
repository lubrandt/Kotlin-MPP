package de.innosystec.kuestion

import de.innosystec.kuestion.network.getAllSurveys
import de.innosystec.kuestion.utility.scope
import kotlinx.coroutines.launch
import react.*
import react.dom.*
import react.router.dom.navLink

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

        div {
            h3 {
                +"List of Mock Surveys (not working)"
            }
            ul {
                li {
                    navLink("${props.basepath}/mockSu/r", exact = true) {
                        +"mockSurvey"
                    }
                }
                li {
                    navLink("${props.basepath}/0123456/r", exact = true) {
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
                        navLink("${props.basepath}/${item.second}/r", exact = true) {
                            +item.first
                        }
                        navLink("${props.basepath}/${item.second}/edit") {
                            +"EditSurvey"
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
    var surveys: List<StringPair>
}
