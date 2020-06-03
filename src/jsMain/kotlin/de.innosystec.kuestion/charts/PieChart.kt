package de.innosystec.kuestion.charts

import de.innosystec.kuestion.*
import de.innosystec.kuestion.network.getResultFromApi
import kotlinx.coroutines.launch
import react.functionalComponent
import react.useEffect
import react.useState

val PieChart = functionalComponent<IdProps> { props ->
    val (chartData, setChartData) = useState(emptyArray<ChartSliceData>())

    useEffect(dependencies = listOf(props.id)) {
        scope.launch {
            setChartData(getResultFromApi(props.id).answers.toTypedArray())
        }
    }

    ReactPieChart {
        attrs.data = chartData
    }

}
