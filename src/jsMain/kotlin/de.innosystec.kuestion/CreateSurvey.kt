package de.innosystec.kuestion

import kotlinext.js.JsObject
import kotlinext.js.jsObject
import kotlinx.coroutines.launch
import kotlinx.html.ButtonFormEncType
import kotlinx.html.ButtonFormMethod
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*


class CreateSurvey : RComponent<RProps, SurveyState>() {

    override fun SurveyState.init() {
        complSurvey = CompleteSurvey()
    }

    override fun RBuilder.render() {


        //Home (show list of surveys?) & Create in one place
        h3 {
            +"Landingpage & Creation of Survey"
        }

//        child(functionalComponent = completeSurveyList)

        div {
            h5 {
                +"Question: "
                +state.complSurvey.question
            }
            h5 {
                +"Answers: "
//                +state.complSurvey.answers.size.toString()
            }
            ul {
                state.complSurvey.answers.forEach { item ->
                    li {
                        key = item
                        +item
                    }
                }
            }

        }


        div {
            // Question
            child(
                functionalComponent = questionComponent,
                props = jsObject {
                    onSubmit = { input ->
                        setState {
                            complSurvey.question = input
                        }
                    }
                }
            )
        }


        div {
            // Answers
            child(functionalComponent = answerComponent,
                props = jsObject {
                    onSubmit = { input ->
                        setState {
                            if (!complSurvey.answers.contains(input)) {
                                complSurvey.answers.add(input)
                            }
                        }
                    }
                }
            )
        }


        button(
            formEncType = ButtonFormEncType.applicationXWwwFormUrlEncoded,
            formMethod = ButtonFormMethod.post,
            type = ButtonType.submit
        ) {
            attrs.formAction = ""
            +"Submit Survey"
        }


    }


}

interface SurveyState : RState {
    var complSurvey: CompleteSurvey
}

interface InputProps : RProps {
    var onSubmit: (String) -> Unit
}