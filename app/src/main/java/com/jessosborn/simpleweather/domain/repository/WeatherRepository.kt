package com.jessosborn.simpleweather.domain.repository

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.jessosborn.simpleweather.R
import com.jessosborn.simpleweather.domain.db.dao.WeatherSnapshotDao
import com.jessosborn.simpleweather.domain.remote.OpenWeatherEndpoint
import com.jessosborn.simpleweather.domain.remote.responses.CurrentWeather
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import com.jessosborn.simpleweather.view.compose.widget.WeatherWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.time.DurationUnit
import kotlin.time.toDuration

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

        val cachedDataResult = getCachedForecastData(zip, units)
        if (cachedDataResult.isSuccess) {
            Log.d("WeatherRepository", "Returning cached data")
            updateWidgetState()
            return cachedDataResult
        }

        // --- If cache is missing, expired, or for a different location, fetch from network ---
        val networkResponse = service.getForecastWeather(location = "$zip,$country", apiKey = key, units = units)

        return try {
            if (networkResponse.isSuccessful) {
                networkResponse.body()?.let { forecastWeather ->
                    withContext(Dispatchers.IO) {
                        val createdAt = System.currentTimeMillis()
                        val snapshotsToInsert = forecastWeather.list.map { snapshot ->
                            snapshot.copy(zip = zip, units = units, createdAt = createdAt)
                        }
                        weatherSnapshotDao.deleteForecast(zip, units)
                        weatherSnapshotDao.insertForecast(snapshotsToInsert)
                        updateWidgetState()
                    }
                    Result.success(forecastWeather)
                } ?: Result.failure(IOException("Response body is null"))
            } else {
                Result.failure(IOException(networkResponse.errorBody()?.string() ?: "API request failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun updateWidgetState() {
        glanceManager.getGlanceIds(WeatherWidget::class.java).forEach { id ->
            Log.d("WeatherRepository", "Updating widget with ID: $id")
            WeatherWidget().update(context, id)
        }
    }

    override suspend fun getCachedForecastData(zip: String, units: String): Result<ForecastWeather> {
        val cachedForecast: List<WeatherSnapshot>? = weatherSnapshotDao.getForecast(zip, units).firstOrNull()

        if (cachedForecast.isNullOrEmpty()) {
            return Result.failure(IOException("No cached data for $zip with units $units"))
        }

        val firstSnapshot = cachedForecast.first()
        val lifeSpan = 4.toDuration(DurationUnit.HOURS)
        val age = (System.currentTimeMillis() - firstSnapshot.createdAt).toDuration(DurationUnit.MILLISECONDS)

        return if (age < lifeSpan) {
            Result.success(ForecastWeather(cachedForecast))
        } else {
            Result.failure(IOException("Cache expired for $zip. Data is $age old."))
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
