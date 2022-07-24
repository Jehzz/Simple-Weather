package com.jessosborn.simpleweather.model.network

import com.jessosborn.simpleweather.model.data.CurrentWeather
import com.jessosborn.simpleweather.model.data.ForecastWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherEndpoint {

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
