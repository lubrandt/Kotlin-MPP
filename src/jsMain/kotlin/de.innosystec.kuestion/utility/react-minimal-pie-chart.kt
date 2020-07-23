@file:JsModule("react-minimal-pie-chart")
@file:JsNonModule

package de.innosystec.kuestion.utility

import react.*
//TODO review: bisschen doku zu js npm modul zu kotlinjs mapping bitte danke
// (nur dass es das mapping ist + hands-on link)
@JsName("default")
external val ReactPieChart: RClass<ReactPieChartProps>

external interface ReactPieChartProps: RProps {
    var data: Array<ChartSlice>
}
