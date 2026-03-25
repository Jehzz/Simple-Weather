package com.jessosborn.simpleweather.view.compose.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jessosborn.simpleweather.domain.Units
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.utils.DayNightPreviews
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme

/**
 * Minimum recommended size is 200.dp square
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun TemperatureGauge(
    modifier: Modifier = Modifier,
    colors: List<Color> =
        listOf(
            Color.White,
            Color(0xFF800080),
            Color.Blue,
            Color.Green,
            Color.Yellow,
            Color.Red,
            Color.Black,
            Color.Black,
        ),
    currentTemp: Int,
    units: Units,
    startAngle: Float = 225f,
    sweepAngle: Float = 270f, // How far the arc travels clockwise
    tickLength: Dp = 20.dp,
    tickWidth: Dp = 4.dp,
    tickSpacing: Dp = 18.dp,
) {
    Surface(
        modifier = modifier.aspectRatio(1f),
        color = Color.Transparent,
    ) {
        BoxWithConstraints(contentAlignment = Alignment.Center) {
            drawDial(
                brush = Brush.sweepGradient(colors),
                currentTemperature = currentTemp,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                tickLength = tickLength,
                tickWidth = tickWidth,
                tickSpacing = tickSpacing,
                units = units,
            )

            // BoxWithConstraints exposes maxDimen, allows text to scale with the component size
            val fontSize = maxWidth.value.sp / 4
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$currentTemp°",
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Composable
private fun drawDial(
    modifier: Modifier = Modifier,
    brush: Brush,
    startAngle: Float, // User input (0 is North)
    sweepAngle: Float,
    tickLength: Dp,
    tickWidth: Dp,
    tickSpacing: Dp,
    currentTemperature: Int,
    units: Units,
) {
    val needleColor = MaterialTheme.colorScheme.onSurface

    // Internal correction: Adjust user's "North = 0" to "3 o'clock = 0" for Canvas API
    val adjustedStartAngle = startAngle - 90f

    Canvas(
        modifier =
            modifier
                .fillMaxSize()
                .padding(10.dp),
    ) {
        // 1. Draw the Dial
        rotate(degrees = adjustedStartAngle) {
            drawArc(
                brush = brush,
                startAngle = 0f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style =
                    Stroke(
                        width = tickLength.toPx(),
                        pathEffect =
                            PathEffect.dashPathEffect(
                                intervals = floatArrayOf(tickWidth.toPx(), tickSpacing.toPx()),
                            ),
                    ),
            )
        }

        // 2. Draw the Needle
        // Define ranges based on the unit system
        val (tempMin, tempMax) =
            if (units == Units.Metric) {
                -20f to 45f // Typical Celsius range
            } else {
                0f to 110f // Typical Fahrenheit range
            }

        val clampedTemp = currentTemperature.toFloat().coerceIn(tempMin, tempMax)

        // Calculate progress: (current - min) / (max - min)
        val progressPercent = (clampedTemp - tempMin) / (tempMax - tempMin)

        val needleRotation = startAngle + (progressPercent * sweepAngle)

        rotate(degrees = needleRotation) {
            val radius = size.width / 2
            val needleWidth = 30.0f
            val needleStartOffset = radius / 2f

            val needlePath =
                Path().apply {
                    val needleBaseY = center.y - needleStartOffset

                    moveTo(center.x - needleWidth, needleBaseY) // Left base
                    lineTo(center.x, tickLength.toPx())
                    lineTo(center.x + needleWidth, needleBaseY) // Right base
                    close()
                }
            drawPath(path = needlePath, color = needleColor)
        }
    }
}

@DayNightPreviews
@Composable
private fun LargeImperialPreview(
    @PreviewParameter(ForecastPreviewParams::class) forecast: ForecastWeather,
) {
    SimpleWeatherTheme {
        Column(
            modifier = Modifier.size(250.dp, 250.dp),
        ) {
            val weather = forecast.list.first().main
            TemperatureGauge(
                currentTemp = weather.temp.toInt(),
                units = Units.Imperial,
            )
        }
    }
}
