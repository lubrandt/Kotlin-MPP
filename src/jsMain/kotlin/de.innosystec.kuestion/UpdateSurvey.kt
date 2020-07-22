package de.innosystec.kuestion

import de.innosystec.kuestion.network.*
import de.innosystec.kuestion.utility.ComponentStyles
import de.innosystec.kuestion.utility.scope
import kotlinext.js.jsObject
import kotlinx.coroutines.launch
import kotlinx.html.InputType
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import react.router.dom.redirect
import styled.css
import styled.styledDiv

class UpdateSurvey : RComponent<IdProps, UpdateSurveyState>() {

    override fun UpdateSurveyState.init() {
        receivedSurvey = SurveyPackage()
        answers = mutableListOf()
        redirect = false
    }

    private fun updateSurvey() {
        scope.launch {
            val response = frontendAPI.getSurveyFromApi(props.id)
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

    override fun RBuilder.render() {
        div {
            div {
                if (state.redirect) redirect("/", "/surveys/allSurveys")
            }
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
                                frontendAPI.endSurvey(props.id)
                            }
                            updateSurvey()

                        }
                    }
                    button {
                        /**
                         * bug?
                         * frontend sends a request to render the survey again
                         * before the redirect happens causing a 400 cause the survey doen's exist anymore
                         */
                        +"Delete Survey"
                        attrs.onClickFunction = {
                            scope.launch {
                                frontendAPI.deleteSurvey(props.id)
                            }
                            updateSurvey()
                            setState {
                                redirect = true
                            }
                        }
                    }
                    button {
                        +"Change Survey"
                        attrs.onClickFunction = {
                            scope.launch {
                                frontendAPI.sendChangedSurvey(
                                    props.id, SurveyPackage(
                                        state.question,
                                        state.answers,
                                        "${state.date}T${state.time}"
                                    )
                                )
                                val response = frontendAPI.getSurveyFromApi(props.id)
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
    var surveyHash: Int
    var date: String
    var time: String

    var redirect: Boolean
}