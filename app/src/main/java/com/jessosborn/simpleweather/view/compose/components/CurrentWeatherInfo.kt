package com.jessosborn.simpleweather.view.compose.components

import android.text.format.DateFormat
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.jessosborn.simpleweather.utils.CombinedPreviews
import com.jessosborn.simpleweather.view.compose.theme.ExtendedTheme
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CurrentWeatherInfo(
    data: CurrentWeather?,
    preferredUnits: Units,
    onSettingsClicked: () -> Unit,
) {
    val convertedTemp =
        if (preferredUnits == Units.Metric) {
            data?.main?.temp?.let { (it - 32) / 1.8f } ?: 0f
        } else {
            data?.main?.temp ?: 0f
        }
    val backgroundColor =
        animateColorAsState(
            targetValue =
                if (convertedTemp < 70.0) {
                    ExtendedTheme.colors.cold
                } else {
                    ExtendedTheme.colors.hot
                },
            label = "Temperature Color",
        )
    Column(
        modifier =
            Modifier
                .background(
                    brush =
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    backgroundColor.value,
                                    backgroundColor.value,
                                    MaterialTheme.colorScheme.background,
                                ),
                        ),
                )
                .fillMaxWidth(1f)
                .padding(8.dp)
                .statusBarsPadding(),
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(start = 12.dp),
                text = data?.name.orEmpty(),
                style = MaterialTheme.typography.displaySmall,
            )
            Icon(
                modifier = Modifier.clickable { onSettingsClicked() },
                painter = painterResource(id = R.drawable.ic_settings_24dp),
                contentDescription = null,
            )
        }
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(all = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                data?.main?.let {
                    TemperatureGauge(
                        modifier = Modifier.fillMaxWidth(0.4f),
                        currentTemp = it.temp.toInt(),
                        units = preferredUnits,
                    )
                }
                Text(
                    text = data?.weather?.get(0)?.main.orEmpty(),
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
            Column {
                Text(
                    text =
                        stringResource(
                            id = R.string.wind_speed,
                            data?.wind?.speed.orEmpty(),
                        ),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text =
                        stringResource(
                            id = R.string.humidity,
                            data?.main?.humidity.orEmpty(),
                        ),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text =
                        stringResource(
                            id = R.string.sunset_time,
                            data?.sys?.sunset?.let { sunset ->
                                DateFormat.format(
                                    (DateFormat.getTimeFormat(LocalContext.current) as SimpleDateFormat).toLocalizedPattern(),
                                    Calendar.getInstance(Locale.ENGLISH).apply {
                                        timeInMillis = sunset.toLong() * 1000L
                                    },
                                ).toString()
                            }.orEmpty(),
                        ),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@CombinedPreviews
@Composable
private fun CurrentWeatherInfoPreview(
    @PreviewParameter(WeatherPreviewParams::class) weather: CurrentWeather,
) {
    SimpleWeatherTheme {
        CurrentWeatherInfo(
            data = weather,
            preferredUnits = Units.Imperial,
            onSettingsClicked = {},
        )
    }
}

class WeatherPreviewParams : PreviewParameterProvider<CurrentWeather> {
    override val values: Sequence<CurrentWeather> =
        sequenceOf(
            CurrentWeather(
                name = "Hollywood",
                main =
                    Main(
                        temp = 73.38f,
                        temp_min = 67.01f,
                        temp_max = 76.87f,
                        humidity = "78",
                    ),
                sys =
                    Sys(
                        country = "US",
                        sunrise = "1674998066",
                        sunset = "1675036678",
                    ),
                weather =
                    listOf(
                        WeatherData(
                            id = 804,
                            main = "Clouds",
                            description = "overcast clouds",
                            icon = "04d",
                        ),
                    ),
                wind =
                    Wind(
                        speed = "14.97",
                        deg = "200",
                    ),
            ),
            CurrentWeather(
                name = "Las Vegas",
                main =
                    Main(
                        temp = 53.38f,
                        temp_min = 50.01f,
                        temp_max = 66.87f,
                        humidity = "40",
                    ),
                sys =
                    Sys(
                        country = "US",
                        sunrise = "1674998066",
                        sunset = "1675036678",
                    ),
                weather =
                    listOf(
                        WeatherData(
                            id = 804,
                            main = "Clouds",
                            description = "overcast clouds",
                            icon = "04d",
                        ),
                    ),
                wind =
                    Wind(
                        speed = "14.97",
                        deg = "200",
                    ),
            ),
        )
}
