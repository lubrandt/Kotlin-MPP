@file:JsModule("react-minimal-pie-chart")
@file:JsNonModule

package de.innosystec.kuestion

import react.*

@JsName("default")
external val PieChart: RClass<PieChartProps>

external interface PieChartProps: RProps {
    var data: Array<ChartSliceData>
}

