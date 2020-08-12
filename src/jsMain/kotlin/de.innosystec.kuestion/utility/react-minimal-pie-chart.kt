@file:JsModule("react-minimal-pie-chart")
@file:JsNonModule

package de.innosystec.kuestion.utility

import react.*
@JsName("default")
external val ReactPieChart: RClass<ReactPieChartProps>

external interface ReactPieChartProps: RProps {
    var data: Array<ChartSlice>
}
