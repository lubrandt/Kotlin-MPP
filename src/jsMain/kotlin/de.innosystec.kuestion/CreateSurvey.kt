package de.innosystec.kuestion

import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinext.js.JsObject
import kotlinext.js.jsObject
import kotlinx.coroutines.launch
import kotlinx.html.ButtonFormEncType
import kotlinx.html.ButtonFormMethod
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*


class CreateSurvey : RComponent<RProps, SurveyState>() {

    override fun SurveyState.init() {
        complSurvey = CompleteSurvey()
        surveyHash = ""
    }

    override fun RBuilder.render() {


        //Home (show list of surveys?) & Create in one place
        h3 {
            +"Landingpage & Creation of Survey"
        }

//        child(functionalComponent = completeSurveyList)

        div {
            p {
                +"Question:"
                br {}
                +state.complSurvey.question

            }
            p {
                +"Answers: "
//                +state.complSurvey.answers.size.toString()
            }
            ul {
                state.complSurvey.answers.forEach { item ->
                    li {
                        key = item
                        attrs.onClickFunction = {
                            setState {
                                state.complSurvey.answers.remove(item)
                            }
                        }
                        +item
                    }
                }
            }
        }


        // Question
        div {
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


        // Answers
        div {
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
            attrs.onClickFunction = {
                if (state.complSurvey.question != "" && state.complSurvey.answers.size > 0) {
                    scope.launch {
                        val resp = sendSurveyToApi(state.complSurvey)
                        setState {
                            surveyHash = resp
                        }
                    }
                }
            }
            +"Submit Survey"
        }

        if (state.surveyHash != "") {
            p {
                +"your hash/id is: ${state.surveyHash}"
            }
            a("/#/${state.surveyHash}", target = "_blank") {
                attrs.rel = "noopener noreferrer"
                +"click here to go to your survey, share this link with participants"
            }
        }

        println("Survey is: \nQuestion: " + state.complSurvey.question + "\nAnswers: " + state.complSurvey.answers)
    }


}

interface SurveyState : RState {
    var complSurvey: CompleteSurvey
    var surveyHash: String
}

interface InputProps : RProps {
    var onSubmit: (String) -> Unit
}