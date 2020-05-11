package de.innosystec.kuestion

import kotlinext.js.jsObject
import kotlinx.coroutines.launch
import kotlinx.html.ButtonType
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*


class CreateSurvey : RComponent<RProps, CreateSurveyState>() {

    override fun CreateSurveyState.init() {
        question = ""
        answers = mutableListOf<String>()
        surveyHash = ""
        expirationDate = ExpirationDate()
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
                +state.expirationDate.toString()
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
                    inputPart = "Question?"
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
                    inputPart = "Answer?"
                }
            )
        }

        // Date
        div {
            child(functionalComponent = inputComponent,
                props = jsObject {
                    onSubmit = { input ->
                        setState {
                            expirationDate.year = input
                        }
                    }
                    inputPart = "Year"
                }
            )
            child(functionalComponent = inputComponent,
                props = jsObject {
                    onSubmit = { input ->
                        var tmp = input
                        if (input.length < 2) {tmp = "0$input"}
                        setState {
                            expirationDate.month = tmp
                        }
                    }
                    inputPart = "Month"
                }
            )
            child(functionalComponent = inputComponent,
                props = jsObject {
                    onSubmit = { input ->
                        var tmp = input
                        if (input.length < 2) {tmp = "0$input"}
                        setState {
                            expirationDate.day = tmp
                        }
                    }
                    inputPart = "Day"
                }
            )
            child(functionalComponent = inputComponent,
                props = jsObject {
                    onSubmit = { input ->
                        var tmp = input
                        if (input.length < 2) {tmp = "0$input"}
                        setState {
                            expirationDate.hour = tmp
                        }
                    }
                    inputPart = "Hour"
                }
            )
            child(functionalComponent = inputComponent,
                props = jsObject {
                    onSubmit = { input ->
                        var tmp = input
                        if (input.length < 2) {tmp = "0$input"}
                        setState {
                            expirationDate.minute = tmp
                        }
                    }
                    inputPart = "Minute"
                }
            )
        }




        button(type = ButtonType.button) {
            +"Submit Survey"
            attrs.onClickFunction = {
                if (state.question.length > 3
                    && state.answers.size >= 2
                    && state.expirationDate.year != ""
                    && state.expirationDate.month != ""
                    && state.expirationDate.day != ""
                    && state.expirationDate.hour != ""
                    && state.expirationDate.minute != ""
                ) {
                    scope.launch {
                        val resp = sendSurveyToApi(
                            SurveyCreation(
                                state.question,
                                state.answers,
                                "${state.expirationDate.year}-${state.expirationDate.month}-${state.expirationDate.day}T${state.expirationDate.hour}:${state.expirationDate.minute}"
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
                    expirationDate = ExpirationDate()
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

        println("Survey is: \nQuestion: " + state.question + "\nAnswers: " + state.answers + "\nExpirationDate: ${state.expirationDate}")
    }


}

interface CreateSurveyState : RState {
    var question: String
    var answers: MutableList<String>
    var surveyHash: String
    var expirationDate: ExpirationDate
}

interface InputProps : RProps {
    var onSubmit: (String) -> Unit
    var inputPart: String
}