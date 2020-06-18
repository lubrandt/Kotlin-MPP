package de.innosystec.kuestion

import de.innosystec.kuestion.network.sendSurveyToApi
import de.innosystec.kuestion.utility.scope
import kotlinext.js.jsObject
import kotlinx.coroutines.launch
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*


class CreateSurvey : RComponent<MainProps, CreateSurveyState>() {

    override fun CreateSurveyState.init() {
        question = ""
        answers = mutableListOf()
        surveyHash = ""
        date = ""
        time = ""
    }

    override fun RBuilder.render() {
        h3 {
            +"Creation of Surveys"
        }

        div {
            p {
                +"Question:"
                br {}
                +state.question

            }
            p {
                +"Answers: "
//                +state.complSurvey.answers.size.toString()
            }
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
            p {
                +"ExpirationDate:"
                br {}
                +"${state.date} | ${state.time}"
            }
        }


        // Question
        div {
            child(
                functionalComponent = inputComponent,
                props = jsObject {
                    onSubmit = { input ->
                        setState {
                            question = input
                        }
                    }
                    inputType = InputType.text
                    inputPlaceholder = "Question?"
                }
            )
        }


        // Answers
        div {
            child(functionalComponent = inputComponent,
                props = jsObject {
                    onSubmit = { input ->
                        val newAnswer = Answer(text = input)
                        setState {
                            if (!answers.contains(newAnswer)) {
                                answers.add(newAnswer)
                            }
                        }
                    }
                    inputType = InputType.text
                    inputPlaceholder = "Answer?"
                }
            )
        }

        // Date
        div {
            //Datum
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

            //Zeit
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



        div {
            button(type = ButtonType.button) {
                +"Submit Survey"
                attrs.onClickFunction = {
                    if (state.question.length > 3
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

        div {
            if (state.surveyHash != "") {
                p {
                    +"your hash/id is: ${state.surveyHash}"
                    br {}
                    +"click the link below to go to your survey, share this link with future participants"
                }
                a("/#${props.basepath}/${state.surveyHash}/r", target = "_blank") {
                    attrs.rel = "noopener noreferrer"
                    +"localhost:8080/#${props.basepath}/${state.surveyHash}/r"
                }
            } else {
                p {
                    +"Please enter a question, at least two answers and a complete Expiration Date."
                }
            }
        }

        println(
            "Survey is: \nQuestion: " + state.question
                    + "\nAnswers: " + state.answers
                    + "\nDate and Time: ${state.date} | ${state.time}"
        )
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