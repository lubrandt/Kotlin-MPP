package de.innosystec.kuestion

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.*
import kotlinx.css.*
import react.*
import react.dom.*
import styled.css
import styled.styledDiv
import kotlin.browser.window

class App : RComponent<RProps, AppState>() {

    override fun AppState.init() {
        unwatchedVideos = listOf(
//            Video(1, "Building and breaking things", "John Doe", "https://youtu.be/PsaFVLr8t4E"),
//            Video(2, "The development process", "Jane Smith", "https://youtu.be/PsaFVLr8t4E"),
//            Video(3, "The Web 7.0", "Matt Miller", "https://youtu.be/PsaFVLr8t4E")
        )
        watchedVideos = listOf(
//            Video(4, "Mouseless development", "Tom Jerry", "https://youtu.be/PsaFVLr8t4E")
        )
        val mainScope = MainScope()
        mainScope.launch {
            val videos = fetchVideos()
            setState {
                unwatchedVideos = videos
            }
        }
    }

    override fun RBuilder.render() {
        h1 {
            +"KotlinConf Explorer/React&KotlinJS tut"
        }
        div {
            h3 {
                +"unwatched Videos"
            }
            child(VideoList::class) {
                attrs.videos = state.unwatchedVideos
                attrs.selectedVideo = state.currentVideo
                attrs.onSelectVideo = { video ->
                    setState {
                        currentVideo = video
                    }
                }
            }
            h3 {
                +"watched Videos"
            }
            videoList {
                videos = state.watchedVideos
                selectedVideo = state.currentVideo
                onSelectVideo = { video ->
                    setState {
                        currentVideo = video
                    }
                }
            }
            state.currentVideo?.let { currentVideo ->
                videoPlayer {
                    video = currentVideo
                    unwatchedVideo = currentVideo in state.unwatchedVideos
                    onWatchedButtonPressed = {
                        if (video in state.unwatchedVideos) {
                            setState {
                                unwatchedVideos -= video
                                watchedVideos += video
                            }
                        } else {
                            setState {
                                unwatchedVideos += video
                                watchedVideos -= video
                            }
                        }
                    }
                }
            }

        }

    }
}

interface AppState : RState {
    var currentVideo: Video?
    var unwatchedVideos: List<Video>
    var watchedVideos: List<Video>
}

suspend fun fetchVideo(id: Int): Video {
    val responsePromise = window.fetch("https://my-json-server.typicode.com/kotlin-hands-on/kotlinconf-json/videos/$id")
    val response = responsePromise.await()
    val jsonPromis = response.json()
    val json = jsonPromis.await()
    return json.unsafeCast<Video>()
}

suspend fun fetchVideos(): List<Video> =
    coroutineScope {
        (1..25).map { id ->
            async {
                fetchVideo(id)
            }
        }.awaitAll()
    }