package com.jessosborn.simpleweather.view.compose.widget

import android.content.Context
import android.text.format.DateFormat
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.wrapContentWidth
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import com.jessosborn.simpleweather.domain.repository.WidgetRepository
import com.jessosborn.simpleweather.utils.getIconResource
import com.jessosborn.simpleweather.view.MainActivity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

class WeatherWidget : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        val weather: Flow<List<WeatherSnapshot>> = WidgetRepository.getRepository(context).getWeather()
        provideContent {
            weather.collectAsState(initial = null).value?.let {
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
        modifier =
            GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.background)
                .clickable(actionStartActivity<MainActivity>()),
    ) {
        weather.take(10).forEach { weather -> GlanceWeatherItem(weather = weather) }
    }
}

@Composable
private fun GlanceWeatherItem(
    modifier: GlanceModifier = GlanceModifier,
    weather: WeatherSnapshot,
) {
    Column(
        modifier =
            modifier
                .padding(vertical = 4.dp, horizontal = 4.dp)
                .wrapContentWidth()
                .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = " ${weather.main.temp.roundToInt()} °",
            style = TextStyle(color = GlanceTheme.colors.onSurface, fontSize = Typography().labelLarge.fontSize),
            maxLines = 1,
        )

        Image(
            modifier = GlanceModifier.defaultWeight().wrapContentWidth(),
            provider = ImageProvider(resId = getIconResource(weather.weather.first().icon)),
            contentDescription = "weather",
        )

        Text(
            text =
                DateFormat.format(
                    (DateFormat.getTimeFormat(LocalContext.current) as SimpleDateFormat).toLocalizedPattern(),
                    Calendar.getInstance(Locale.ENGLISH).apply {
                        timeInMillis = weather.dt.toLong() * 1000L
                    },
                ).toString(),
            style = TextStyle(color = GlanceTheme.colors.onSurface, fontSize = Typography().labelSmall.fontSize),
            maxLines = 1,
        )
    }
}
