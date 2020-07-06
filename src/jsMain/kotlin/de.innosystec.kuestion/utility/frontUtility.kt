package de.innosystec.kuestion.utility

import de.innosystec.kuestion.Answer
import kotlin.js.Date
import kotlin.random.Random

val dateOfToday = Date().toISOString().substring(0, 10)

internal fun randHexColor(): String {
    val stringLength = 6
    val charPool: List<Char> = ('a'..'f') + ('0'..'9')
    val randomString =
        (1..stringLength)
            .map { Random.nextInt(0, charPool.size) }
            .map { x -> charPool[x] }
            .joinToString("")
    return "#${randomString}"
}

data class ChartSlice(
    val title: String, //standard init
    val value: Int,
    val color: String
)

fun createChartSliceArray(answers: MutableList<Answer>): Array<ChartSlice> {
    val chartList = mutableListOf<ChartSlice>()
    val colorList = mutableListOf<String>()
    answers.forEach {
        chartList.add(ChartSlice(it.text,it.counts, randHexColor())) // todo: consistent color?
    }
    return chartList.toTypedArray()
}

val dataMock: List<ChartSlice> = mutableListOf(
    ChartSlice("One", 10, "#E38627"),
    ChartSlice("Two", 15, "#C13C37"),
    ChartSlice("Three", 20, "#6A2135")
)