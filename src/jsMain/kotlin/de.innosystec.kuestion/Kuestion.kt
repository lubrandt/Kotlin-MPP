package de.innosystec.kuestion

import de.innosystec.kuestion.charts.*
import de.innosystec.kuestion.spa.Home
import de.innosystec.kuestion.spa.Stuff
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinext.js.asJsObject
import kotlinx.coroutines.*
import kotlinx.css.*
import kotlinx.html.ButtonFormMethod
import kotlinx.html.ButtonType
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import react.*
import react.dom.*
import react.router.dom.*
import styled.*
import kotlin.browser.window

class Kuestion : RComponent<IdProps, KuestionState>() {
    //SPA seems to be a bad idea now. Everything is already rendered when Home is created. NOt the desired behavior

    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    override fun KuestionState.init() {
        val mainScope = MainScope()
        mainScope.launch {
            val getResponse = client.get<String>("${jvmBackend}/")
            setState {
                response = getResponse
            }
        }
    }

    override fun RBuilder.render() {
        hashRouter {
            div {
                h3 {
                    +"Kuestion First Blood"
                }
                p {
                    +"You have received: ${state.response}"
                }
                ul("header") {
                    li {
                        navLink("/", exact = true) {
                            +"HomeCreateApp"
                        }
                    }
                    li {
                        navLink("/mockSu", exact = true) {
                            +"mockSurvey"
                        }
                    }
                    li {
                        navLink("/0123456", exact = true) {
                            +"tooLongSurvey"
                        }
                    }
                }
                div("content") {
                    route("/", CreateSurvey::class, exact = true)
                    route<IdProps>("/:id") { props ->
                        child(Chart::class) {

                            attrs.id = props.match.params.id
//                            coScope.join()
//                            println("outsied coro: ${attrs.dataGraph}")

                        }
                    }

                }
            }
        }

    }
}


interface KuestionState : RState {
    var response: String?
}

suspend fun fetchResponse(id: String): Array<ChartSliceData> =
    window.fetch("${jvmBackend}/${id}").await().json().await().unsafeCast<Array<ChartSliceData>>()

