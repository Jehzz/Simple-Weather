package com.jessosborn.simpleweather.domain.remote

import com.jessosborn.simpleweather.domain.remote.responses.CurrentWeather
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherEndpoint {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("zip") location: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
    ): Response<CurrentWeather>

    @GET("forecast")
    suspend fun getForecastWeather(
        @Query("zip") location: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
    ): Response<ForecastWeather>

    companion object {
        const val baseApiUrl: String = "https://api.openweathermap.org/data/2.5/"
    }
}
