package com.jessosborn.simpleweather.view.compose.components

import android.text.format.DateFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.jessosborn.simpleweather.R
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import com.jessosborn.simpleweather.utils.CombinedPreviews
import com.jessosborn.simpleweather.utils.getIconResource
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
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier =
                modifier
                    .padding(all = 4.dp)
                    .fillMaxWidth()
                    .clickable { onClick(item) },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(id = R.string.degrees, item.main.temp.roundToInt()),
                style = MaterialTheme.typography.titleMedium,
            )
            Image(
                painter = painterResource(id = getIconResource(item.weather.first().icon)),
                contentDescription = item.weather.first().main,
                modifier = Modifier.size(52.dp)
            )
            val timeFormat = (DateFormat.getTimeFormat(LocalContext.current) as SimpleDateFormat).toLocalizedPattern()
            val time = DateFormat.format(
                timeFormat,
                Calendar.getInstance(Locale.ENGLISH).apply {
                    timeInMillis = item.dt.toLong() * 1000L
                },
            )
            Text(
                text = time.toString(),
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@CombinedPreviews
@Composable
private fun WeatherItemPreview(
    @PreviewParameter(ForecastPreviewParams::class) forecast: ForecastWeather,
) {
    SimpleWeatherTheme {
        LazyVerticalGrid(columns = GridCells.Fixed(4)) {
            items(forecast.list.take(4)) { item ->
                WeatherItem(
                    modifier = Modifier.padding(4.dp),
                    item = item,
                    onClick = {},
                )
            }
        }
    }
}
