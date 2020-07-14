package de.innosystec.kuestion

import kotlinx.serialization.Serializable

const val jvmHost = "127.0.0.1"
const val jvmPort = 8081

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

@Serializable
data class StringPair(
    val first: String, //survey, question
    val second: String //answer, hash
)

object CommonDateUtil

expect class CommonDate

expect fun CommonDateUtil.checkDate(z: CommonDate): Boolean
// todo: ist diese funktion testbar in commonTest? wird eine implementation benötigt? shared test for expected values
