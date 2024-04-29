package com.jessosborn.simpleweather.domain.repository

import com.jessosborn.simpleweather.domain.remote.responses.CurrentWeather
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather

interface IWeatherRepository {
    suspend fun fetchCurrentData(zip: String, country: String, units: String): Result<CurrentWeather>
    suspend fun fetchForecastData(zip: String, country: String, units: String): Result<ForecastWeather>
}
