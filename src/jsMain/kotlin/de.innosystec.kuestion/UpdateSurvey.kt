package de.innosystec.kuestion

import de.innosystec.kuestion.network.changedSurvey
import de.innosystec.kuestion.network.deleteSurvey
import de.innosystec.kuestion.network.endSurvey
import de.innosystec.kuestion.network.getResultFromApi
import de.innosystec.kuestion.utility.ComponentStyles
import de.innosystec.kuestion.utility.scope
import kotlinext.js.jsObject
import kotlinx.coroutines.launch
import kotlinx.html.InputType
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import styled.css
import styled.styledDiv

class UpdateSurvey : RComponent<IdProps, UpdateSurveyState>() {

    override fun UpdateSurveyState.init() {
        receivedSurvey = SurveyPackage()
        answers = mutableListOf()
    }

    private fun updateSurvey() {
        scope.launch {
            val response = getResultFromApi(props.id)
            setState {
                receivedSurvey = response
                question = receivedSurvey.question
                answers = receivedSurvey.answers
                surveyHash = props.id
                date = receivedSurvey.expirationTime.substring(0,10)
                time = receivedSurvey.expirationTime.substring(11)
            }
        }
    }

    override fun componentDidMount() {
        updateSurvey()
    }

    //todo: redirect from this component if the survey doesn't exist anymore?
    override fun RBuilder.render() {
        div {
            div {
                h3 {
                    +"Editing your Survey here:"
                }
            }
            div {
                styledDiv {
                    css {
                        +ComponentStyles.displaySurveyInfo
                    }
                    styledDiv {
                        css {
                            +ComponentStyles.Surveyinfo
                        }
                        table {
                            tbody {

                                tr {
                                    td {
                                        +"Question:"
                                    }
                                    td {
                                        +state.question
                                    }
                                }
                                tr {
                                    td {
                                        +"Answers:"

                                    }
                                    td {
                                        ul {
                                            state.answers.forEach { item ->
                                                li {
                                                    key = item.text
                                                    attrs.onClickFunction = {
                                                        setState {
                                                            state.answers.remove(item)
                                                        }
                                                    }
                                                    +item.text
                                                }
                                            }
                                        }
                                    }
                                }
                                tr {
                                    td {
                                        +"Expiration Date:"
                                    }
                                    td {
                                        +state.date
                                    }
                                }
                                tr {
                                    td {
                                        +"Time:"

                                    }
                                    td {
                                        +state.time
                                    }
                                }
                            }
                        }

                    }

                    styledDiv {
                        css {
                            +ComponentStyles.inputFields
                        }
                        div {
                            child(
                                functionalComponent = inputComponent,
                                props = jsObject {
                                    onSubmit = { input ->
                                        setState {
                                            if (input.length in 1..25) {
                                                question = input
                                            }
                                        }
                                    }
                                    inputType = InputType.text
                                    inputPlaceholder = "Question?"
                                }
                            )
                        }
                        div {
                            child(functionalComponent = inputComponent,
                                props = jsObject {
                                    onSubmit = { input ->
                                        val newAnswer = Answer(text = input)
                                        setState {
                                            if (!answers.contains(newAnswer) && input.length in 1..50) {
                                                answers.add(newAnswer)
                                            }
                                        }
                                    }
                                    inputType = InputType.text
                                    inputPlaceholder = "Answer?"
                                }
                            )
                        }
                        div {
                            child(functionalComponent = inputComponent,
                                props = jsObject {
                                    onChange = { input ->
                                        setState {
                                            date = input
                                        }
                                    }
                                    inputPlaceholder = ""
                                    inputType = InputType.date
                                }

                            )

                            child(functionalComponent = inputComponent,
                                props = jsObject {
                                    onChange = { input ->
                                        setState {
                                            time = input
                                        }
                                    }
                                    inputPlaceholder = ""
                                    inputType = InputType.time
                                }
                            )
                        }
                    }
                }
                styledDiv {
                    css {
                        +ComponentStyles.buttons
                    }
                    button {
                        +"End Survey"
                        attrs.onClickFunction = {
                            scope.launch {
                                endSurvey(props.id)
                            }
                            updateSurvey()
                        }
                    }
                    button {
                        +"Delete Survey"
                        attrs.onClickFunction = {
                            scope.launch {
                                deleteSurvey(props.id)
                            }
                            updateSurvey()
                        }
                    }
                    button {
                        +"Change Survey"
                        //todo: what if survey was deleted?
                        attrs.onClickFunction = {
                            scope.launch {
                                changedSurvey(
                                    props.id, SurveyPackage(
                                        state.question,
                                        state.answers,
                                        "${state.date}T${state.time}"
                                    )
                                )
                                val response = getResultFromApi(props.id)
                                setState {
                                    receivedSurvey = response
                                    question = receivedSurvey.question
                                    answers = receivedSurvey.answers
                                    surveyHash = props.id
                                    date = receivedSurvey.expirationTime.substring(0,10)
                                    time = receivedSurvey.expirationTime.substring(11)
                                }
                            }
                        }
                    }
                }
            }
        }

//        println("Survey is: \nQuestion: " + state.question
//                + "\nAnswers: " + state.answers
//                + "\nDate and Time: ${state.date} | ${state.time}"
//        )
    }
}


/**
 * this method is used to call this Component like a usual Component ( displaySurvey { } )
 */
fun RBuilder.updateSurvey(handler: IdProps.() -> Unit): ReactElement {
    return child(UpdateSurvey::class) {
        this.attrs(handler)
    }
}

interface UpdateSurveyState: RState {
    var receivedSurvey: SurveyPackage
    var question: String
    var answers: MutableList<Answer>
    var surveyHash: String
    var date: String
    var time: String
}