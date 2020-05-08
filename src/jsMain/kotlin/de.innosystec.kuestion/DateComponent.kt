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
import kotlin.js.Date

val dateComponent = functionalComponent<InputProps> {props ->
    val (dateTime, setDateTime) = useState("")

    val submitDateHandler: (Event) -> Unit = {
        it.preventDefault()
        setDateTime("")
        val date = Date(dateTime)
//        println(dateTime)
//        println(date)
        props.onSubmit(date.toString())
    }

    val changeDateHandler: (Event) -> Unit = {
        val value = (it.target as HTMLInputElement).value
        setDateTime(value)
    }

    form {
        attrs.onSubmitFunction = submitDateHandler
        input(InputType.text) {
            attrs.onChangeFunction = changeDateHandler
            attrs.value = dateTime
            attrs.placeholder = "YYYY-MM-DDTHH:MM"
        }
    }
}