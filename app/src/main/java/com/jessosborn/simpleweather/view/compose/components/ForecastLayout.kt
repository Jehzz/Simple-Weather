package com.jessosborn.simpleweather.view.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun ForecastLayout(forecastWeather: ForecastWeather?) {

	var showDialog by remember { mutableStateOf(false) }
	var selectedSnapshot by remember { mutableStateOf<WeatherSnapshot?>(null) }

	AnimatedVisibility(
		visible = showDialog,
		enter = fadeIn(),
		exit = fadeOut()
	) {
		selectedSnapshot?.let {
			WeatherDetailDialog(
				weatherSnapshot = it,
				onDismiss = { showDialog = false }
			)
		}
	}

	BoxWithConstraints {
		val gridCount = when {
			maxWidth > 700.dp -> 8
			maxWidth > 500.dp -> 6
			else -> 4
		}
		Column {
			Text(
				text = stringResource(id = R.string.todays_forecast),
				modifier = Modifier.fillMaxWidth(1f),
				textAlign = TextAlign.Center,
				style = MaterialTheme.typography.titleLarge
			)
			forecastWeather?.list?.let { forecast ->
				LazyVerticalGrid(columns = GridCells.Fixed(gridCount)) {
					items(forecast.take(8)) { item ->
						WeatherItem(
							modifier = Modifier.padding(4.dp),
							item = item,
							onClick = {
								selectedSnapshot = it
								showDialog = true
							}
						)
					}
				}
				Text(
					text = stringResource(id = R.string.tomorrows_weather),
					modifier = Modifier.fillMaxWidth(1f),
					textAlign = TextAlign.Center,
					style = MaterialTheme.typography.titleLarge
				)
				LazyVerticalGrid(columns = GridCells.Fixed(gridCount)) {
					items(forecast.drop(8)) { item ->
						WeatherItem(
							modifier = Modifier.padding(4.dp),
							item = item,
							onClick = {
								selectedSnapshot = it
								showDialog = true
							}
						)
					}
				}
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
			ForecastLayout(forecastWeather = forecast)
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
				)
			)
		)
	)
}
