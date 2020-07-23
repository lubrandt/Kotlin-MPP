package de.innosystec.kuestion.utility

import de.innosystec.kuestion.Answer
import kotlin.js.Date
import kotlin.random.Random

fun Answer.Companion.toChartSliceArray(answers: MutableList<Answer>): Array<ChartSlice> {
    val chartList = mutableListOf<ChartSlice>()
    answers.forEach {
        chartList.add(ChartSlice(it.text, it.counts, randHexColor()))
    }
    return chartList.toTypedArray()
}

fun dateOfToday() = Date().toISOString().substring(0, 10)

private fun randHexColor(): String {
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
    val title: String,
    val value: Int,
    val color: String
)




