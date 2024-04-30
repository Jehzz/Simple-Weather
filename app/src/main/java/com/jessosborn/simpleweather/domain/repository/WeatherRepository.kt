package com.jessosborn.simpleweather.domain.repository

import android.content.Context
import com.jessosborn.simpleweather.R
import com.jessosborn.simpleweather.domain.remote.OpenWeatherEndpoint
import com.jessosborn.simpleweather.domain.remote.responses.CurrentWeather
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import java.io.IOException

class WeatherRepository(
    context: Context,
    private val service: OpenWeatherEndpoint
) : IWeatherRepository {

    private val key = context.resources.getString(R.string.api_key)

    override suspend fun fetchForecastData(zip: String, country: String, units: String): Result<ForecastWeather> {
        val response = service.getForecastWeather("$zip,$country", key, units)
        return if (response.isSuccessful) {
            response.body()?.let {
                Result.success(it)
            } ?: Result.failure(IOException("Body null"))
        } else {
            Result.failure(IOException("Fail"))
        }
    }

    override suspend fun fetchCurrentData(zip: String, country: String, units: String): Result<CurrentWeather> {
        val response = service.getCurrentWeather("$zip,$country", key, units)
        return if (response.isSuccessful) {
            response.body()?.let {
                Result.success(it)
            } ?: Result.failure(IOException("Body null"))
        } else {
            Result.failure(IOException("Fail"))
        }
    }
}
