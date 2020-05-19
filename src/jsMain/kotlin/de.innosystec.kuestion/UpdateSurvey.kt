package de.innosystec.kuestion

import kotlinx.coroutines.launch
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.button
import react.dom.div
import react.dom.link
import react.router.dom.navLink

class UpdateSurvey :RComponent<IdProps,RState>() {
    override fun RBuilder.render() {
        div {
            button {
                +"End Survey"
                //todo: actual expected time with common code(color beispiel)??
                //todo: basic-auth with name == pw
                attrs.onClickFunction =  {
                    scope.launch {
                        endSurvey(props.id)
                    }
                }
            }
            button {
                +"Delete Survey"
                attrs.onClickFunction = {
                    scope.launch {
                        deleteSurvey(props.id)
                        //todo: automatic redirect?
                    }
                }
            }
        }
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