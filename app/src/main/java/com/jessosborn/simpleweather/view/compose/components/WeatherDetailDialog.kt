package com.jessosborn.simpleweather.view.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
                Row(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        val id = "icon_${weatherSnapshot.weather.first().icon}_t"
                        val icon = painterResource(LocalContext.current.resources.getIdentifier(id, "drawable", LocalContext.current.packageName))
                        Image(
                            painter = icon,
                            contentDescription = weatherSnapshot.weather.first().main,
                            modifier = Modifier.size(86.dp)
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
