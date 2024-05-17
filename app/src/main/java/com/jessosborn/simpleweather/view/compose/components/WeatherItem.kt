package com.jessosborn.simpleweather.view.compose.components

import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jessosborn.simpleweather.R
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import com.jessosborn.simpleweather.utils.DayNightPreviews
import com.jessosborn.simpleweather.utils.debugPlaceholder
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun WeatherItem(
	modifier: Modifier = Modifier,
	item: WeatherSnapshot,
	onClick: (WeatherSnapshot) -> Unit,
) {
	Surface(
		color = MaterialTheme.colorScheme.background
	) {
		Column(
			modifier = modifier
				.padding(all = 4.dp)
				.fillMaxWidth()
				.clickable { onClick(item) },
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.SpaceAround
		) {
			Text(
				text = stringResource(id = R.string.degrees, item.main.temp.roundToInt()),
				style = MaterialTheme.typography.titleLarge
			)
			AsyncImage(
				model = "https://openweathermap.org/img/wn/${item.weather[0].icon}@4x.png",
				placeholder = debugPlaceholder(debugPreview = R.drawable.ic_settings_24dp),
				contentDescription = item.weather.first().main
			)
			val timeFormat = (DateFormat.getTimeFormat(LocalContext.current) as SimpleDateFormat).toLocalizedPattern()
			Text(
				text = DateFormat.format(
					timeFormat,
					Calendar.getInstance(Locale.ENGLISH).apply {
						timeInMillis = item.dt.toLong() * 1000L
					}
				).toString(),
				style = MaterialTheme.typography.titleMedium
			)
		}
	}
}


@DayNightPreviews
@Composable
fun WeatherItemPreview(@PreviewParameter(ForecastPreviewParams::class) forecast: ForecastWeather) {
	SimpleWeatherTheme {
		LazyVerticalGrid(columns = GridCells.Fixed(4)) {
			items(forecast.list.take(4)) { item ->
				WeatherItem(
					modifier = Modifier.padding(4.dp),
					item = item,
					onClick = {}
				)
			}
		}
	}
}
