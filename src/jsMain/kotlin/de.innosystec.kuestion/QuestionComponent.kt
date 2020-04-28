package de.innosystec.kuestion

import kotlinx.html.InputType
import kotlinx.html.js.*
import kotlinx.html.mark
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*

val questionComponent = functionalComponent<InputProps> { props ->
    val (question, setQuestion) = useState("")

    val submitHandler: (Event) -> Unit = {
        it.preventDefault()
        setQuestion("")
        props.onSubmit(question)
    }

    val changeHandler: (Event) -> Unit = {
        val value = (it.target as HTMLInputElement).value
        setQuestion(value)
    }

    form {
        attrs.onSubmitFunction = submitHandler
        input(InputType.text) {
            attrs.onChangeFunction = changeHandler
            attrs.value = question
            attrs.placeholder = "Question?"
        }
    }
}