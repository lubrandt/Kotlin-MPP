package de.innosystec.kuestion

import kotlinx.serialization.Serializable

//TODO review: in config files für js (für proof-of-concept ignorierbar)
const val jvmHost = "127.0.0.1"
const val jvmPort = 8081


//TODO review: datenklassen in eigenes file "model.kt"
@Serializable
data class Answer(
    val survey: String = "",
    val text: String = "",
    val counts: Int = 0
)

@Serializable
data class SurveyPackage(
    var question: String = "",
    var answers: MutableList<Answer> = mutableListOf(),
    var expirationTime: String = ""
)

//TODO review: auftrennen in pair für survey/answer und question/hash
@Serializable
data class StringPair(
    val first: String, //survey, question
    val second: String //answer, hash
)

//TODO review: common.kt in expected-CommonDate.kt
object CommonDateUtil

expect class CommonDate

//TODO review: umbennen, was wird hier gecheckt
expect fun CommonDateUtil.checkDate(z: CommonDate): Boolean
// todo: ist diese funktion testbar in commonTest? wird eine implementation benötigt? shared test for expected values
