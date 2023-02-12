package com.jessosborn.simpleweather.view.compose.composables

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.jessosborn.simpleweather.R
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.domain.remote.responses.Main
import com.jessosborn.simpleweather.domain.remote.responses.WeatherData
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme

@Composable
fun ForecastLayout(forecastWeather: ForecastWeather?) {
	Surface {
		Column {
			Row(
				modifier = Modifier
					.align(Alignment.CenterHorizontally)
					.padding(vertical = 8.dp),
			) {
				Text(
					text = stringResource(id = R.string.todays_forecast),
					style = MaterialTheme.typography.titleMedium
				)
			}
			LazyVerticalGrid(columns = GridCells.Fixed(4)) {
				forecastWeather?.list?.let { list ->
					itemsIndexed(list) { index, item ->
						val iconId = list[index].weather[0].icon
						WeatherItem(
							modifier = Modifier.padding(4.dp),
							imgUrl = "https://openweathermap.org/img/wn/$iconId@4x.png",
							item = item
						)
					}
				}
			}
		}
	}
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ForecastPreview(
	@PreviewParameter(ForecastPreviewParams::class) forecast: ForecastWeather,
) {
	SimpleWeatherTheme {
		ForecastLayout(forecastWeather = forecast)
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
				)
			)
		)
	)
}
