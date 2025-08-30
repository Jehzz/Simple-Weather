package com.jessosborn.simpleweather.view.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jessosborn.simpleweather.R
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import com.jessosborn.simpleweather.utils.CombinedPreviews
import com.jessosborn.simpleweather.utils.debugPlaceholder
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
                Row(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .scale(1.5f)
                                .padding(12.dp),
                            model = "https://openweathermap.org/img/wn/${weatherSnapshot.weather[0].icon}@4x.png",
                            placeholder = debugPlaceholder(debugPreview = R.drawable.ic_settings_24dp),
                            contentDescription = weatherSnapshot.weather.first().main
                        )
                        Text(
                            text = weatherSnapshot.weather.first().description,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Column {
                        Text(text = "Temperature : ${weatherSnapshot.main.temp}°")
                        Text(text = "Humidity : ${weatherSnapshot.main.humidity}")
                        Text(text = "Precipitation : ${(weatherSnapshot.pop * 100).toInt()}%")
                        weatherSnapshot.snow?.let {
                            Text(text = "Snow : ${it.`3h`}mm")
                        }
                        weatherSnapshot.rain?.let {
                            Text(text = "Rain : ${it.`3h`}mm")
                        }
                    }
                }
            }
        },
    )
}

@CombinedPreviews
@Composable
private fun WeatherDetailDialogPreview(
    @PreviewParameter(ForecastPreviewParams::class) forecast: ForecastWeather,
) {
    SimpleWeatherTheme {
        WeatherDetailDialog(
            weatherSnapshot = forecast.list.first(),
            onDismiss = {},
        )
    }
}
