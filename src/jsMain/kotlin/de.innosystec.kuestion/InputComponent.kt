package de.innosystec.kuestion

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.dom.form
import react.dom.input
import react.functionalComponent
import react.useState

val inputComponent = functionalComponent<InputProps> {props ->
    val (input, setInput) = useState("")

    val submitDateHandler: (Event) -> Unit = {
        it.preventDefault()
        setInput("")
        props.onSubmit(input)
    }

    val changeDateHandler: (Event) -> Unit = {
        val value = (it.target as HTMLInputElement).value
        setInput(value)
    }

    form {
        attrs.onSubmitFunction = submitDateHandler
        input(InputType.text) {
            attrs.onChangeFunction = changeDateHandler
            attrs.value = input
            attrs.placeholder = props.inputPart
        }
    }
}