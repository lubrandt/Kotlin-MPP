package de.innosystec.kuestion

import kotlinext.js.jsObject
import kotlinx.coroutines.launch
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import kotlin.js.Date


class CreateSurvey : RComponent<RProps, CreateSurveyState>() {

    override fun CreateSurveyState.init() {
        question = ""
        answers = mutableListOf<String>()
        surveyHash = ""
        date = ""
        time = ""
    }

    override fun RBuilder.render() {


        //Home (show list of surveys?) & Create in one place
        h3 {
            +"Landingpage & Creation of Survey"
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
                        key = item
                        attrs.onClickFunction = {
                            setState {
                                state.answers.remove(item)
                            }
                        }
                        +item
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
                        setState {
                            if (!answers.contains(input)) {
                                answers.add(input)
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
            //todo: no submit on both
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
                            SurveyCreation(
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
                    answers = mutableListOf<String>()
                    surveyHash = ""
                    date = ""
                    time = ""
                }
            }
        }

        if (state.surveyHash != "") {
            p {
                +"your hash/id is: ${state.surveyHash}"
                br {}
                +"click the link below to go to your survey, share this link with future participants"
            }
            a("/#/${state.surveyHash}/r", target = "_blank") {
                attrs.rel = "noopener noreferrer"
                +"localhost:8080/#/${state.surveyHash}/r"
            }
        } else {
            p {
                +"Please enter a question, at least two answers and a complete Expiration Date."
            }
        }

        println("Survey is: \nQuestion: " + state.question
                + "\nAnswers: " + state.answers
                + "\nDate and Time: ${state.date} | ${state.time}"
        )
    }


}

interface CreateSurveyState : RState {
    var question: String
    var answers: MutableList<String>
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