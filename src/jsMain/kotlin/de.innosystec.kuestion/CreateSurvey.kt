package de.innosystec.kuestion

import de.innosystec.kuestion.network.sendSurveyToApi
import de.innosystec.kuestion.utility.ComponentStyles
import de.innosystec.kuestion.utility.scope
import kotlinext.js.jsObject
import kotlinx.coroutines.launch
import kotlinx.css.margin
import kotlinx.css.px
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.js.*
import react.*
import react.dom.*
import styled.*


class CreateSurvey : RComponent<MainProps, CreateSurveyState>() {

    override fun CreateSurveyState.init() {
        question = ""
        answers = mutableListOf()
        surveyHash = ""
        date = ""
        time = ""
    }

    override fun RBuilder.render() {
        div {
            div {
                h3 {
                    +"Create your Survey here"
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
                                        val pattern = "dd-mm-yyyy"
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
                    styledDiv {
                        css {
                            +ComponentStyles.flex100pct
                        }
                        button(type = ButtonType.button) {
                            +"Submit Survey"
                            attrs.onClickFunction = {
                                if (state.question.length in 4..25
                                    && state.answers.size >= 2
                                    && state.date != ""
                                    && state.time != ""
                                ) {
                                    scope.launch {
                                        val resp = sendSurveyToApi(
                                            SurveyPackage(
                                                state.question,
                                                state.answers,
                                                "${state.date}T${state.time}"
                                            )
                                        )
                                        setState {
                                            surveyHash = resp
                                        }
                                    }
                                }
                            }
                        }
                        button(type = ButtonType.button) {
                            +"Reset/New Survey"
                            attrs.onClickFunction = {
                                setState {
                                    question = ""
                                    answers = mutableListOf()
                                    surveyHash = ""
                                    date = ""
                                    time = ""
                                }
                            }
                        }
                    }
                    styledDiv {
                        css {
                            +ComponentStyles.flex100pct
                        }
                        if (state.surveyHash != "") {
                            p {
                                +"your hash/id is: ${state.surveyHash}"
                                br {}
                                +"click the link below to go to your survey, share this link with future participants"
                            }
                            a("/#${props.basepath}/${state.surveyHash}/r", target = "_self") {
                                attrs.rel = "noopener noreferrer"
                                +"localhost:8080/#${props.basepath}/${state.surveyHash}/r"
                            }
                        } else {
                            p {
                                +"Please enter a question, at least two answers and a complete Expiration Date."
                            }
                        }
                    }
                }

            }
        }


//        println(
//            "Survey is: \nQuestion: " + state.question
//                    + "\nAnswers: " + state.answers
//                    + "\nDate and Time: ${state.date} | ${state.time}"
//        )
    }
}

fun RBuilder.createSurvey(handler: MainProps.() -> Unit): ReactElement {
    return child(CreateSurvey::class) {
        this.attrs(handler)
    }
}

interface CreateSurveyState : RState {
    var question: String
    var answers: MutableList<Answer>
    var surveyHash: String
    var date: String
    var time: String
}

interface InputProps : RProps {
    var onSubmit: (String) -> Unit
    var onChange: (String) -> Unit
    var inputPlaceholder: String
    var inputType: InputType
}