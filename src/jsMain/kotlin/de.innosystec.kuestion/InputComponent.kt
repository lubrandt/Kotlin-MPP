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

val inputComponent = functionalComponent<InputProps> { props ->
    val (input, setInput) = useState("")

    val submitHandler: (Event) -> Unit = {
        it.preventDefault()
        setInput("")
        if (props.inputType == InputType.text || props.inputType == InputType.text) {
            props.onSubmit(input)
        }
    }

    val changeHandler: (Event) -> Unit = {
        val value = (it.target as HTMLInputElement).value
        setInput(value)
        if (props.inputType == InputType.date || props.inputType == InputType.time) {
            props.onChange(value)
        }
    }

    form {
        attrs.onSubmitFunction = submitHandler
        input(props.inputType) {
            attrs.onChangeFunction = changeHandler
            attrs.value = input
            attrs.placeholder = props.inputPlaceholder
        }
    }
}