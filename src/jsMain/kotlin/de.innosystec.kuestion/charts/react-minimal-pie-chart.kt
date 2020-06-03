@file:JsModule("react-minimal-pie-chart")
@file:JsNonModule

package de.innosystec.kuestion.charts

import de.innosystec.kuestion.ChartSliceData
import react.*

@JsName("default")
external val ReactPieChart: RClass<ReactPieChartProps>

external interface ReactPieChartProps: RProps {
    var data: Array<ChartSliceData>
}
//todo: ChartSLice Data nur im Frontend, Answer Object über Leitung, chartslice im frontend erstellt
