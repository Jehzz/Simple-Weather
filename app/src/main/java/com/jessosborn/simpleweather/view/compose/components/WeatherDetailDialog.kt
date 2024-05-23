package com.jessosborn.simpleweather.view.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import com.jessosborn.simpleweather.utils.CombinedPreviews
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun WeatherDetailDialog(
	weatherSnapshot: WeatherSnapshot,
	onDismiss: () -> Unit,
) {
	BasicAlertDialog(
		onDismissRequest = { onDismiss() },
		content = {
			Card {
				Column(
					modifier = Modifier.padding(12.dp)
				) {
					Text(text = "Temperature : ${weatherSnapshot.main.temp}")
					Text(text = "Humidity : ${weatherSnapshot.main.humidity}")
					Text(text = weatherSnapshot.weather.first().description)
				}
			}
		}
	)
}

@CombinedPreviews
@Composable
private fun WeatherDetailDialogPreview(@PreviewParameter(ForecastPreviewParams::class) forecast: ForecastWeather) {
	SimpleWeatherTheme {
		WeatherDetailDialog(
			weatherSnapshot = forecast.list.first(),
			onDismiss = {}
		)
	}
}