package de.innosystec.kuestion.charts

import de.innosystec.kuestion.ChartSliceData
import de.innosystec.kuestion.IdProps
import de.innosystec.kuestion.getResultFromApi
import de.innosystec.kuestion.scope
import kotlinx.coroutines.launch
import react.functionalComponent
import react.useEffect
import react.useState

val PieChart = functionalComponent<IdProps> { props ->
    val (chartData, setChartData) = useState(emptyArray<ChartSliceData>())

    useEffect(dependencies = listOf(props.id)) {
        scope.launch {
            setChartData(getResultFromApi(props.id))
        }
    }

    ReactPieChart {
        attrs.data = chartData
    }

}
