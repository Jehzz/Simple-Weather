package com.jessosborn.simpleweather.view.compose.composables

import android.content.res.Configuration
import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.jessosborn.simpleweather.R
import com.jessosborn.simpleweather.domain.Units
import com.jessosborn.simpleweather.domain.remote.responses.CurrentWeather
import com.jessosborn.simpleweather.domain.remote.responses.Main
import com.jessosborn.simpleweather.domain.remote.responses.Sys
import com.jessosborn.simpleweather.domain.remote.responses.WeatherData
import com.jessosborn.simpleweather.domain.remote.responses.Wind
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme
import com.jessosborn.simpleweather.view.compose.theme.coldColor
import com.jessosborn.simpleweather.view.compose.theme.hotColor
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun CurrentWeatherInfo(
	data: CurrentWeather?,
	preferredUnits: Units,
	onSettingsClicked: () -> Unit,
) {
	Surface {
		Column(
			modifier = Modifier
				.background(
					color = when (preferredUnits) {
						Units.Metric -> if ((data?.main?.temp ?: 0.0f) < 21.1) {
							MaterialTheme.colors.coldColor
						} else {
							MaterialTheme.colors.hotColor
						}

						Units.Imperial -> if ((data?.main?.temp ?: 0.0f) < 70.0) {
							MaterialTheme.colors.coldColor
						} else {
							MaterialTheme.colors.hotColor
						}
					}
				)
				.fillMaxWidth(1f)
				.padding(all = 4.dp)
		) {
			Row(
				modifier = Modifier.padding(all = 4.dp),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Text(
					modifier = Modifier.weight(1f),
					text = data?.name.orEmpty(),
					style = MaterialTheme.typography.h4
				)
				Icon(
					modifier = Modifier.clickable { onSettingsClicked() },
					painter = painterResource(id = R.drawable.ic_settings_24dp),
					contentDescription = null
				)
			}
			Row {
				Column(
					modifier = Modifier.weight(1f),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Text(
						text = data?.main?.temp?.let {
							stringResource(id = R.string.degrees, it.roundToInt())
						}.orEmpty(),
						style = MaterialTheme.typography.h4
					)
					Text(
						text = data?.weather?.get(0)?.main.orEmpty(),
						style = MaterialTheme.typography.h6
					)
				}
				Column(
					modifier = Modifier
						.weight(1f)
						.padding(start = 4.dp)
				) {
					Text(
						text = stringResource(
							id = R.string.wind_speed,
							data?.wind?.speed.orEmpty()
						)
					)
					Text(
						text = stringResource(
							id = R.string.humidity,
							data?.main?.humidity.orEmpty()
						)
					)
					Text(
						text = stringResource(
							id = R.string.sunset_time,
							data?.sys?.sunset?.let { sunset ->
								DateFormat.format(
									"HH:mm",
									Calendar.getInstance(Locale.ENGLISH).apply {
										timeInMillis = sunset.toLong() * 1000L
									}
								).toString()
							}.orEmpty()
						)
					)
				}
			}
		}
	}
}

@Preview(name = "light", showBackground = true)
@Preview(name = "dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CurrentWeatherInfoPreview(
	@PreviewParameter(WeatherPreviewParams::class) weather: CurrentWeather,
) {
	SimpleWeatherTheme {
		CurrentWeatherInfo(
			data = weather,
			preferredUnits = Units.Imperial,
			onSettingsClicked = {}
		)
	}
}


class WeatherPreviewParams : PreviewParameterProvider<CurrentWeather> {
	override val values: Sequence<CurrentWeather> = sequenceOf(
		CurrentWeather(
			name = "Hollywood",
			main = Main(
				temp = 73.38f,
				temp_min = "67.01",
				temp_max = "76.87",
				humidity = "78"
			),
			sys = Sys(
				country = "US",
				sunrise = "1674998066",
				sunset = "1675036678"
			),
			weather = listOf(
				WeatherData(
					id = 804,
					main = "Clouds",
					description = "overcast clouds",
					icon = "04d"
				)
			),
			wind = Wind(
				speed = "14.97",
				deg = "200"
			)
		),
		CurrentWeather(
			name = "Las Vegas",
			main = Main(
				temp = 53.38f,
				temp_min = "50.01",
				temp_max = "66.87",
				humidity = "40"
			),
			sys = Sys(
				country = "US",
				sunrise = "1674998066",
				sunset = "1675036678"
			),
			weather = listOf(
				WeatherData(
					id = 804,
					main = "Clouds",
					description = "overcast clouds",
					icon = "04d"
				)
			),
			wind = Wind(
				speed = "14.97",
				deg = "200"
			)
		)
	)
}