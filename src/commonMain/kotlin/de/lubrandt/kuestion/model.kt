package de.lubrandt.kuestion

import kotlinx.serialization.Serializable

@Serializable
data class Answer(
    val survey: Int = -1,
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
data class SurveyAnswerPair(
    val survey: Int,
    val answer: String
)

@Serializable
data class HashQuestionPair(
    val hash: Int,
    val question: String
)