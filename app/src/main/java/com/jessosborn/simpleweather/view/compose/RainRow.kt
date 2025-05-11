package com.jessosborn.simpleweather.view.compose

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import com.jessosborn.simpleweather.view.compose.components.ForecastPreviewParams
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun RainRow(
	weatherSnapshots: List<WeatherSnapshot>,
	modifier: Modifier = Modifier,
	height: Dp = 100.dp
) {
	Column(
		modifier = modifier
			.border(
				width = 1.dp,
				color = Color.Gray,
				shape = RoundedCornerShape(10.dp),
			)
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.padding(start = 2.dp, end = 2.dp, top = 10.dp)
				.wrapContentHeight()
		) {
			Canvas(
				modifier = Modifier
					.fillMaxWidth()
					.height(height)
			) {
				// Filter the weather snapshots to get only the Rain data.
				// Default null values to zero for Collections functions
				val precipitationData = weatherSnapshots.map { snapshot ->
					snapshot.rain?.`3h`?.let { rain ->
						Pair(snapshot.dt, rain)
					} ?: Pair(snapshot.dt, 0.0f)
				}
				if (precipitationData.isEmpty()) {
					drawYAxisLabels(maxPrecipitation = 1f, axisColor = Color.Gray)
				} else {
					// Find Max Precipitation Value
					val maxPrecipitation = precipitationData.maxOfOrNull { it.second } ?: 1f

					drawYAxisLabels(maxPrecipitation, axisColor = Color.Gray)

					drawXAxisLabels(
						timestamps = precipitationData.map { it.first },
						axisColor = Color.Gray
					)

					drawLineGraph(
						precipitationData = precipitationData,
						maxPrecipitation = maxPrecipitation,
						lineColor = Color.Black
					)
				}

			}
		}
	}
}

private fun DrawScope.drawYAxisLabels(maxPrecipitation: Float, axisColor: Color) {
	val labelCount = 5 // Number of labels to display
	val labelInterval = maxPrecipitation / (labelCount - 1)
	val labelHeightInterval = size.height / (labelCount - 1)

	for (i in 0 until labelCount) {
		val labelValue = (labelInterval * i).roundToInt() // Round to nearest mm
		val y = size.height - (labelHeightInterval * i)

		// Draw label text, excluding the last
		if (i != 0) {
			drawContext.canvas.nativeCanvas.apply {
				drawText(
					"$labelValue mm",
					5f, // x +5 slight offset to get the first digit off of any drawn borders
					y + 10, // y +10 to center the label
					Paint().apply {
						textSize = 12.sp.toPx()
						color = axisColor.hashCode()
						textAlign = Paint.Align.LEFT
					}
				)
			}
		}

		// Draw horizontal line for the label
		drawLine(
			color = axisColor,
			start = Offset(x = 0f, y = y),
			end = Offset(x = size.width, y = y),
			strokeWidth = 1f
		)
	}
}

private fun DrawScope.drawXAxisLabels(timestamps: List<String>, axisColor: Color) {
	if (timestamps.isEmpty()) return
	val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

	val labelCount = 5 // Number of labels to display
	val labelInterval = (timestamps.size - 1) / (labelCount - 1).toFloat()
	val labelWidthInterval = size.width / (labelCount - 1)

	for (i in 0 until labelCount) {
		val index = (labelInterval * i).roundToInt()
		// Check if index is valid
		if (index >= timestamps.size || index < 0) {
			return
		}

		val x = labelWidthInterval * i

		// Draw vertical line for the label
		drawLine(
			color = axisColor,
			start = Offset(x = x, y = 0f),
			end = Offset(x = x, y = size.height),
			strokeWidth = 1f
		)
	}
}

private fun DrawScope.drawLineGraph(
	precipitationData: List<Pair<String, Float>>,
	maxPrecipitation: Float,
	lineColor: Color
) {
	if (precipitationData.isEmpty()) return

	val graphPath = Path()
	val xInterval = size.width / (precipitationData.size - 1)
	val yMultiplier = size.height / maxPrecipitation

	precipitationData.forEachIndexed { index, dataPoint ->
		val (timestamp, precipitation) = dataPoint
		val x = xInterval * index
		val y = size.height - (precipitation * yMultiplier)

		if (index == 0) {
			graphPath.moveTo(x, y)
		} else {
			graphPath.lineTo(x, y)
		}
	}

	drawPath(
		path = graphPath,
		color = lineColor,
		style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
	)
}

@Preview(showBackground = true)
@Composable
fun PrecipitationLineGraphPreviewNoData(@PreviewParameter(ForecastPreviewParams::class) forecast: ForecastWeather) {
	RainRow(
		weatherSnapshots = forecast.list,
		modifier = Modifier.fillMaxWidth()
	)
}