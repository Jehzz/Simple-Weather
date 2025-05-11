package com.jessosborn.simpleweather.domain.repository

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.jessosborn.simpleweather.R
import com.jessosborn.simpleweather.domain.db.dao.WeatherSnapshotDao
import com.jessosborn.simpleweather.domain.remote.OpenWeatherEndpoint
import com.jessosborn.simpleweather.domain.remote.responses.CurrentWeather
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.view.compose.widget.WeatherWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class WeatherRepository(
    private val context: Context,
    private val service: OpenWeatherEndpoint,
    private val weatherSnapshotDao: WeatherSnapshotDao,
    private val glanceManager: GlanceAppWidgetManager,
) : IWeatherRepository {
    private val key = context.resources.getString(R.string.api_key)

    override suspend fun fetchForecastData(
        zip: String,
        country: String,
        units: String,
    ): Result<ForecastWeather> {
        val response = service.getForecastWeather(location = "$zip,$country", apiKey = key, units = units)
        return try {
            if (response.isSuccessful) {
                response.body()?.let {
                    withContext(Dispatchers.IO) {
                        weatherSnapshotDao.deleteAll()
                        weatherSnapshotDao.insert(it.list)
                        glanceManager.getGlanceIds(GlanceAppWidget::class.java).forEach { id ->
                            WeatherWidget().update(context, id)
                        }
                    }
                    Result.success(it)
                } ?: run {
                    Result.failure(IOException("Body null"))
                }
            } else {
                Result.failure(IOException(response.errorBody()?.string() ?: "Fail"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchCurrentData(
        zip: String,
        country: String,
        units: String,
    ): Result<CurrentWeather> {
        val response = service.getCurrentWeather(location = "$zip,$country", apiKey = key, units = units)
        return try {
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: run {
                    Result.failure(IOException("Body null"))
                }
            } else {
                return Result.failure(IOException(response.errorBody()?.string() ?: "Fail"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}
