package com.jessosborn.simpleweather.domain.remote

import com.jessosborn.simpleweather.domain.remote.responses.CurrentWeather
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherEndpoint {

    @GET("weather")
    fun getCurrentWeather(
        @Query("zip") location: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
    ): Call<CurrentWeather>

    @GET("forecast")
    fun getForecastWeather(
        @Query("zip") location: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
    ): Call<ForecastWeather>

    companion object {
        const val baseApiUrl: String = "https://api.openweathermap.org/data/2.5/"
    }
}
