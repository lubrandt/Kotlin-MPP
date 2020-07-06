package de.innosystec.kuestion

import kotlinext.js.JsObject
import kotlinext.js.jsObject
import react.*
import react.dom.br
import react.dom.details
import react.dom.h1
import react.dom.style

class ErrorBoundary : RComponent<ErrorProps, ErrorState>() {
    companion object : RStatics<ErrorProps, ErrorState, ErrorBoundary, Nothing>(ErrorBoundary::class) {
        init {
            getDerivedStateFromError = {
                println("getDerivedFromError here")
                ErrorState(hasError = true)
            }
        }
    }

    override fun ErrorState.init() {
        hasError = false
//        error = Throwable()
    }

    override fun componentDidCatch(error: Throwable, info: RErrorInfo) {
        println("======thrown======")
        println("$error -> $info")
    }

    override fun RBuilder.render() {
        h1 {+"ErrorBoundary here"}
        if (props.hasError) {
            h1 {
                +"Something went wrong"
            }
            details {
                style("whitespace")
//                +"${state.error}: ${state.errorInfo}"
                br {}
//                +"${state.errorInfo.componentStack}"
            }
        } else {
            child(props.child)
//            Children.only(props.children)
            //todo: how to render children?
//            childList.forEach {
//                if (it is ReactElement) {
//                      child(it as ReactElement)
//                }
//            }
//            try {
//
//            } catch (e:Exception) {
////                useAsyncError(e)
//            }
//            clear("")
        }
    }
}

//val useAsyncError = functionalComponent<ErrorProps> {props ->
//    val (_,setError) = useState(Throwable())
//    setError(props.error)
//    val erFunc: (Throwable) -> Throwable = {e -> throw e}
//    val func : (Throwable) -> Unit = {e -> setError(throw e)}
//    useCallback({setError(props.error)}, arrayOf(setError))
//}

//fun asyncError(e:Exception): () -> Unit = run {
//    val (_,setError) = useState(Throwable())
    // JSModule & JsNonModule ?!
//    return useCallback({setError(e)}, arrayOf(setError))
//}


fun RBuilder.errorBoundary(handler: ErrorProps.() -> Unit): ReactElement {
    return child(ErrorBoundary::class) {
        this.attrs(handler)
        //what to do with the component inside?
    }
}

data class ErrorState(
    var hasError: Boolean
//    var error: Throwable
//    var errorInfo: RErrorInfo
) : RState

interface ErrorProps : RProps {
    var child: ReactElement
    var hasError: Boolean
    var error: Throwable
}