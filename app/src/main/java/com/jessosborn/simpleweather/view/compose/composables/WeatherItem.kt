package com.jessosborn.simpleweather.view.compose.composables

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.text.format.DateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jessosborn.simpleweather.R
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import com.jessosborn.simpleweather.utils.debugPlaceholder
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun WeatherItem(
	modifier: Modifier = Modifier,
	imgUrl: String,
	item: WeatherSnapshot,
) {
	Card(
		modifier = modifier.padding(all = 8.dp),
	) {
		Column(
			modifier = Modifier.padding(vertical = 4.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(
				text = stringResource(id = R.string.degrees, item.main.temp.roundToInt()),
				style = MaterialTheme.typography.h5
			)
			AsyncImage(
				model = imgUrl,
				placeholder = debugPlaceholder(debugPreview = R.drawable.ic_settings_24dp),
				contentDescription = item.weather.first().main
			)
			Text(
				text = DateFormat.format(
					"HH:mm",
					Calendar.getInstance(Locale.ENGLISH).apply {
						timeInMillis = item.dt.toLong() * 1000L
					}
				).toString(),
				style = MaterialTheme.typography.body1
			)
		}
	}
}


@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun WeatherItemPreview(
	@PreviewParameter(ForecastPreviewParams::class) forecast: ForecastWeather,
) {
	val iconUrl = forecast.list.first().weather.first().icon
	SimpleWeatherTheme {
		WeatherItem(
			imgUrl = "https://openweathermap.org/img/wn/$iconUrl@4x.png",
			item = forecast.list.first()
		)
	}
}
