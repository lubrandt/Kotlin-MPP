package de.innosystec.kuestion

import kotlinx.html.InputType
import kotlinx.html.js.*
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.dom.*
import react.*

val answerComponent = functionalComponent<InputProps> { props ->
    val (answer, setAnswer) = useState("")

    val submitHandler: (Event) -> Unit = {
        it.preventDefault()
        setAnswer("")
        props.onSubmit(answer)
    }

    val changeHandler: (Event) -> Unit = {
        val value = (it.target as HTMLInputElement).value
        setAnswer(value)
    }

    form {

        attrs.onSubmitFunction = submitHandler
        input(InputType.text) {
            attrs.onChangeFunction = changeHandler
            attrs.value = answer
            attrs.placeholder = "Answer?"
        }
    }


}

