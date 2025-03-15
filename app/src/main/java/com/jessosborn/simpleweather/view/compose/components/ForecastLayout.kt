package com.jessosborn.simpleweather.view.compose.components

import android.text.format.DateUtils
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.jessosborn.simpleweather.R
import com.jessosborn.simpleweather.domain.Units
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.domain.remote.responses.Main
import com.jessosborn.simpleweather.domain.remote.responses.WeatherData
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import com.jessosborn.simpleweather.utils.CombinedPreviews
import com.jessosborn.simpleweather.utils.DataStoreUtil
import com.jessosborn.simpleweather.view.compose.theme.ExtendedTheme
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme
import java.text.SimpleDateFormat
import java.util.Calendar

@Composable
fun ForecastLayout(
	forecastWeather: ForecastWeather,
	onSnapshotSelected: (WeatherSnapshot) -> Unit
) {
	val partitionedWeather = forecastWeather.list.partition { DateUtils.isToday(it.dt.toLong() * 1000) }
	val todaysWeather = partitionedWeather.first
	val tomorrowsWeather = partitionedWeather.second.take(8)
	val dayAftersWeather = partitionedWeather.second.drop(8).take(8)

	Column(
		modifier = Modifier.padding(all = 8.dp),
		verticalArrangement = Arrangement.spacedBy(12.dp)
	) {
		if (todaysWeather.isNotEmpty()) {
			ForecastHeader(text = stringResource(id = R.string.todays_forecast))
			ForecastRow(weather = todaysWeather, onSnapshotSelected = onSnapshotSelected)
		}
		ForecastHeader(text = stringResource(id = R.string.tomorrows_weather))
		ForecastRow(weather = tomorrowsWeather, onSnapshotSelected = onSnapshotSelected)
		ForecastHeader(text = stringResource(id = R.string.the_day_after))
		ForecastRow(weather = dayAftersWeather, onSnapshotSelected = onSnapshotSelected)
	}
}

@Composable
private fun ForecastHeader(text: String) {
	Text(
		text = text,
		modifier = Modifier.fillMaxWidth(1f),
		textAlign = TextAlign.Center,
		style = MaterialTheme.typography.titleLarge
	)
}

@Composable
private fun ForecastRow(
	weather: List<WeatherSnapshot>,
	onSnapshotSelected: (WeatherSnapshot) -> Unit
) {
	LazyRow(
		modifier = Modifier
			.fillMaxWidth()
			.border(
				width = 1.dp,
				brush = Brush.horizontalGradient(
					colors = forecastBorderColors(
						temps = Triple(weather.firstOrNull(), weather.getOrNull(weather.size / 2), weather.lastOrNull())
					)
				),
				shape = RoundedCornerShape(10.dp)
			),
		horizontalArrangement = Arrangement.SpaceEvenly
	) {
		items(weather) { item ->
			WeatherItem(
				modifier = Modifier.padding(4.dp),
				item = item,
				onClick = { onSnapshotSelected(it) }
			)
		}
	}
}


@Composable
private fun forecastBorderColors(
	temps: Triple<WeatherSnapshot?, WeatherSnapshot?, WeatherSnapshot?>
): List<Color> {
	val startingColor = getColorFromTemp(temps.first?.main?.temp ?: 0f)
	val middleColor = getColorFromTemp(temps.second?.main?.temp ?: 0f)
	val endingColor = getColorFromTemp(temps.third?.main?.temp ?: 0f)
	return listOf(startingColor, middleColor, endingColor)
}

@Composable
private fun getColorFromTemp(temp: Float): Color {
	val units by DataStoreUtil.getUnits(LocalContext.current).collectAsState(initial = Units.Imperial)

	return when (units) {
		Units.Metric -> if (temp < 21.1) {
			ExtendedTheme.colors.cold
		} else {
			ExtendedTheme.colors.hot
		}

		Units.Imperial -> if (temp < 70.0) {
			ExtendedTheme.colors.cold
		} else {
			ExtendedTheme.colors.hot
		}
	}
}

@CombinedPreviews
@Composable
fun ForecastPreview(@PreviewParameter(ForecastPreviewParams::class) forecast: ForecastWeather) {
	SimpleWeatherTheme {
		Surface {
			ForecastLayout(
				forecastWeather = forecast,
				onSnapshotSelected = {}
			)
		}
	}
}

class ForecastPreviewParams : PreviewParameterProvider<ForecastWeather> {

	private val currentTime = Calendar.getInstance().timeInMillis
	private val timeValue = ArrayList<Long>().apply {
		add(currentTime)
		for (index in 1..23) add(currentTime + (10800 * index))
	}

	override val values: Sequence<ForecastWeather> = sequenceOf(
		ForecastWeather(
			listOf(
				WeatherSnapshot(
					dt = timeValue[0].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[0]),
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[1].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[1]),
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[2].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[2]),
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[3].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[3]),
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[4].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[4]),
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[5].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[5]),
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[6].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[6]),
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[7].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[7]),
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[8].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[8]),
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[9].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[9]),
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[10].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[10]),
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[11].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[11]),
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[12].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[12]),
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[13].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[13]),
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[14].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[14]),
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[15].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[15]),
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[16].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[16]),
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[17].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[17]),
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[18].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[18]),
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[19].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[19]),
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[20].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[20]),
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[21].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[21]),
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[22].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[22]),
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = timeValue[23].toString(),
					dt_txt = SimpleDateFormat.getDateInstance().format(timeValue[23]),
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				)
			)
		)
	)
}
