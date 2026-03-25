package com.jessosborn.simpleweather.view.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.utils.DayNightPreviews
import com.jessosborn.simpleweather.view.compose.components.ForecastPreviewParams
import com.jessosborn.simpleweather.view.compose.theme.ExtendedTheme
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme
import kotlin.math.roundToInt

@Composable
fun PrecipitationGraph(
    data: List<Float>,
    modifier: Modifier = Modifier,
    verticalSteps: Int = 4,
    height: Dp = 100.dp,
    horizontalStepSize: Dp = height / verticalSteps,
) {
    Row(
        modifier =
            modifier
                .height(height)
                .border(
                    width = 1.dp,
                    color = ExtendedTheme.colors.cold,
                    shape = RoundedCornerShape(10.dp),
                ),
    ) {
        val xAxis =
            AxisData.Builder()
                .steps(data.size - 1)
                .axisOffset(0.dp)
                .axisStepSize(horizontalStepSize)
                .bottomPadding(0.dp)
                .topPadding(0.dp)
                .backgroundColor(Color.Transparent)
                .build()

        val yAxis =
            AxisData.Builder()
                .backgroundColor(Color.Transparent)
                .steps(verticalSteps)
                .startPadding(10.dp)
                .axisOffset(20.dp)
                .bottomPadding(0.dp)
                .topPadding(0.dp)
                .axisLabelDescription { "Rain" }
                .axisLabelColor(MaterialTheme.colorScheme.onSurface)
                .labelData { index ->
                    val labelScale = (index.toFloat() / verticalSteps.toFloat())
                    val max = data.maxOf { it }
                    val data = (max * (labelScale)).toString()
                    when {
                        index == verticalSteps || index == 0 || verticalSteps.div(2) == index -> "$data mm"
                        else -> ""
                    }
                }
                .build()

        val lineChartData =
            LineChartData(
                xAxisData = xAxis,
                yAxisData = yAxis,
                paddingTop = 10.dp,
                bottomPadding = 0.dp,
                backgroundColor = MaterialTheme.colorScheme.background,
                gridLines = GridLines(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                linePlotData =
                    LinePlotData(
                        plotType = PlotType.Line,
                        lines =
                            listOf(
                                Line(
                                    dataPoints =
                                        data.mapIndexed { index, data ->
                                            Point(
                                                x = index.toFloat(),
                                                y = data,
                                                description = "$data mm in $index hours",
                                            )
                                        },
                                    lineStyle =
                                        LineStyle(
                                            color = MaterialTheme.colorScheme.secondary,
                                            width = 3f,
                                            lineType = LineType.SmoothCurve(),
                                        ),
                                    intersectionPoint = IntersectionPoint(color = MaterialTheme.colorScheme.tertiary),
                                    selectionHighlightPoint = SelectionHighlightPoint(color = MaterialTheme.colorScheme.primary),
                                    selectionHighlightPopUp =
                                        SelectionHighlightPopUp(
                                            backgroundColor = MaterialTheme.colorScheme.background,
                                            labelColor = MaterialTheme.colorScheme.onSurface,
                                            popUpLabel = { hour, data -> "$data mm in ${hour.roundToInt() + 1} hours" },
                                            paddingBetweenPopUpAndPoint = 16.dp,
                                        ),
                                    shadowUnderLine =
                                        ShadowUnderLine(
                                            alpha = 0.5f,
                                            brush =
                                                Brush.verticalGradient(
                                                    colors = listOf(MaterialTheme.colorScheme.inversePrimary, Color.Transparent),
                                                ),
                                        ),
                                ),
                            ),
                    ),
            )
        LineChart(
            modifier = Modifier.fillMaxSize(),
            lineChartData = lineChartData,
        )
    }
}

@DayNightPreviews
@Composable
fun PrecipitationLineGraphPreviewNoData(
    @PreviewParameter(ForecastPreviewParams::class) forecast: ForecastWeather,
) {
    SimpleWeatherTheme {
        PrecipitationGraph(
            data = forecast.list.map { it.rain?.`3h` ?: 0f },
            modifier = Modifier.fillMaxWidth(),
            height = 200.dp,
        )
    }
}
