package de.innosystec.kuestion

import de.innosystec.kuestion.network.changedSurvey
import de.innosystec.kuestion.network.deleteSurvey
import de.innosystec.kuestion.network.endSurvey
import de.innosystec.kuestion.network.getResultFromApi
import de.innosystec.kuestion.utility.scope
import kotlinext.js.jsObject
import kotlinx.coroutines.launch
import kotlinx.html.InputType
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*

class UpdateSurvey : RComponent<IdProps, UpdateSurveyState>() {

    override fun UpdateSurveyState.init() {
        receivedSurvey = SurveyPackage()
//        question = ""
        answers = mutableListOf()
//        surveyHash = ""
//        date = ""
//        time = ""
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
        h3 {
            +"Editing Survey for ID [${props.id}]:"
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

        div {
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

        println("Survey is: \nQuestion: " + state.question
                + "\nAnswers: " + state.answers
                + "\nDate and Time: ${state.date} | ${state.time}"
        )
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