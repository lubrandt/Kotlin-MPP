package de.innosystec.kuestion

import kotlinx.serialization.Serializable

const val jvmHost = "127.0.0.1"
const val jvmPort = 8081

@Serializable
data class ChartSliceData(
    val title: String,
    val value: Int,
    val color: String
)

val dataMock: List<ChartSliceData> = mutableListOf(
    ChartSliceData("One", 10, "#E38627"),
    ChartSliceData("Two", 15, "#C13C37"),
    ChartSliceData("Three", 20, "#6A2135")
)

@Serializable
data class SurveyCreation(
    var question: String = "",
    var answers: MutableList<String> = mutableListOf<String>()
)

@Serializable
data class SurveyReceiving(
    var question: String = "",
    var answers: MutableList<ChartSliceData> = mutableListOf<ChartSliceData>()
)