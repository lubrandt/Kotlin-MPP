package de.innosystec.kuestion.charts

data class ChartSliceData(
    val title: String,
    val value: Int,
    val color: String
)

val dataMock: Array<ChartSliceData> = arrayOf(
    ChartSliceData("One", 10, "#E38627"),
    ChartSliceData("Two", 15, "#C13C37"),
    ChartSliceData("Three", 20, "#6A2135")
)