package com.jessosborn.simpleweather.view.compose.widget

import android.content.Context
import android.graphics.Bitmap
import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.wrapContentWidth
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import coil.imageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import com.jessosborn.simpleweather.domain.repository.WidgetRepository
import com.jessosborn.simpleweather.view.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

class WeatherWidget : GlanceAppWidget() {

	override suspend fun provideGlance(context: Context, id: GlanceId) {
		val weather: Flow<List<WeatherSnapshot>> = WidgetRepository.getRepository(context).getWeather()
		provideContent {
			val weather = weather.collectAsState(initial = null).value
			weather?.let {
				GlanceTheme {
					Content(it)
				}
			}
		}
	}
}

@Composable
private fun Content(weather: List<WeatherSnapshot>) {
	Row(
		modifier = GlanceModifier
			.fillMaxSize()
			.background(GlanceTheme.colors.background)
			.clickable { actionStartActivity<MainActivity>() }
	) {
		weather.take(10).forEach { weather -> GlanceWeatherItem(weather = weather) }
	}
}


@Composable
private fun GlanceWeatherItem(modifier: GlanceModifier = GlanceModifier, weather: WeatherSnapshot) {
	val context = LocalContext.current
	var imageUrl = "https://openweathermap.org/img/wn/${weather.weather.firstOrNull()?.icon}@4x.png"
	var icon by remember(imageUrl) { mutableStateOf<Bitmap?>(null) }

	LaunchedEffect(imageUrl) {
		withContext(Dispatchers.IO) {
			val request = ImageRequest.Builder(context).data(imageUrl).build()
			icon = when (val result = context.imageLoader.execute(request)) {
				is ErrorResult -> null
				is SuccessResult -> result.drawable.toBitmapOrNull()
			}
		}
	}
	Column(
		modifier = modifier
			.padding(vertical = 2.dp, horizontal = 4.dp)
			.wrapContentWidth()
			.fillMaxHeight(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalAlignment = Alignment.CenterVertically,
	) {
		Text(
			text = " ${weather.main.temp.roundToInt()} °",
			style = TextStyle(color = GlanceTheme.colors.onSurface, fontSize = 14.sp),
			maxLines = 1
		)
		icon?.let { bitmap ->
			Image(
				provider = ImageProvider(bitmap),
				contentDescription = null,
				modifier = GlanceModifier.size(38.dp)
			)
		} ?: run {
			CircularProgressIndicator()
		}
		Text(
			text = DateFormat.format(
				(DateFormat.getTimeFormat(context) as SimpleDateFormat).toLocalizedPattern(),
				Calendar.getInstance(Locale.ENGLISH).apply {
					timeInMillis = weather.dt.toLong() * 1000L
				}
			).toString(),
			style = TextStyle(color = GlanceTheme.colors.onSurface, fontSize = 8.sp),
			maxLines = 1
		)
	}
}