package com.jessosborn.simpleweather.view.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.jessosborn.simpleweather.R
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.domain.remote.responses.Main
import com.jessosborn.simpleweather.domain.remote.responses.WeatherData
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import com.jessosborn.simpleweather.utils.CombinedPreviews
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme

@Composable
fun ForecastLayout(
	forecastWeather: ForecastWeather,
	onSnapshotSelected: (WeatherSnapshot) -> Unit
) {
	val todaysWeather = forecastWeather.list.take(8)
	val tomorrowsWeather = forecastWeather.list.drop(8).take(8)
	val dayAftersWeather = forecastWeather.list.drop(16).take(8)

	Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
		Text(
			text = stringResource(id = R.string.todays_forecast),
			modifier = Modifier.fillMaxWidth(1f),
			textAlign = TextAlign.Center,
			style = MaterialTheme.typography.titleLarge
		)
		LazyRow(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceEvenly
		) {
			items(todaysWeather) { item ->
				WeatherItem(
					modifier = Modifier.padding(4.dp),
					item = item,
					onClick = { onSnapshotSelected(it) }
				)
			}
		}

		Text(
			text = stringResource(id = R.string.tomorrows_weather),
			modifier = Modifier.fillMaxWidth(1f),
			textAlign = TextAlign.Center,
			style = MaterialTheme.typography.titleLarge
		)
		LazyRow(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceEvenly
		) {
			items(tomorrowsWeather) { item ->
				WeatherItem(
					modifier = Modifier.padding(4.dp),
					item = item,
					onClick = { onSnapshotSelected(it) }
				)
			}
		}
		Text(
			text = stringResource(id = R.string.the_day_after),
			modifier = Modifier.fillMaxWidth(1f),
			textAlign = TextAlign.Center,
			style = MaterialTheme.typography.titleLarge
		)
		LazyRow(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceEvenly
		) {
			items(dayAftersWeather) { item ->
				WeatherItem(
					modifier = Modifier.padding(4.dp),
					item = item,
					onClick = { onSnapshotSelected(it) }
				)
			}
		}
	}
}

@CombinedPreviews
@Composable
fun ForecastPreview(
	@PreviewParameter(ForecastPreviewParams::class) forecast: ForecastWeather,
) {
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

	override val values: Sequence<ForecastWeather> = sequenceOf(
		ForecastWeather(
			listOf(
				WeatherSnapshot(
					dt = "1635220400",
					dt_txt = "2023-02-01 00:00:00",
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = "1661331600",
					dt_txt = "2022-08-24 06:00:00",
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = "1675220400",
					dt_txt = "2023-02-01 00:00:00",
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = "1661331600",
					dt_txt = "2022-08-24 06:00:00",
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = "1675220400",
					dt_txt = "2023-02-01 00:00:00",
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = "1661331600",
					dt_txt = "2022-08-24 06:00:00",
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = "1675220400",
					dt_txt = "2023-02-01 00:00:00",
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = "1661331600",
					dt_txt = "2022-08-24 06:00:00",
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = "1635220400",
					dt_txt = "2023-02-01 00:00:00",
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = "1661331600",
					dt_txt = "2022-08-24 06:00:00",
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = "1675220400",
					dt_txt = "2023-02-01 00:00:00",
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = "1661331600",
					dt_txt = "2022-08-24 06:00:00",
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = "1675220400",
					dt_txt = "2023-02-01 00:00:00",
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = "1661331600",
					dt_txt = "2022-08-24 06:00:00",
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = "1675220400",
					dt_txt = "2023-02-01 00:00:00",
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = "1661331600",
					dt_txt = "2022-08-24 06:00:00",
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = "1635220400",
					dt_txt = "2023-02-01 00:00:00",
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = "1661331600",
					dt_txt = "2022-08-24 06:00:00",
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = "1675220400",
					dt_txt = "2023-02-01 00:00:00",
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = "1661331600",
					dt_txt = "2022-08-24 06:00:00",
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = "1675220400",
					dt_txt = "2023-02-01 00:00:00",
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = "1661331600",
					dt_txt = "2022-08-24 06:00:00",
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				),
				WeatherSnapshot(
					dt = "1675220400",
					dt_txt = "2023-02-01 00:00:00",
					main = Main(humidity = "89", temp = 88.8f, temp_min = "80", temp_max = "90"),
					weather = listOf(
						WeatherData(804, "Clouds", "overcast clouds", "04n")
					)
				),
				WeatherSnapshot(
					dt = "1661331600",
					dt_txt = "2022-08-24 06:00:00",
					main = Main(humidity = "89", temp = 50.0f, temp_min = "40", temp_max = "90"),
					weather = listOf(
						WeatherData(800, "Clear", "clear sky", "01n")
					)
				)
			)
		)
	)
}
