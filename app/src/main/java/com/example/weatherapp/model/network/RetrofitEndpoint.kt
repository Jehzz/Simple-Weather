package com.example.weatherapp.model.network

import com.example.weatherapp.model.repository.CurrentWeatherData
import com.example.weatherapp.model.repository.ForecastWeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitEndpoint {

    @GET("forecast")
    fun getForecastWeather(
        @Query("zip") zipCode: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): Call<ForecastWeatherData>

    @GET("weather")
    fun getCurrentWeather(
        @Query("zip") zipCode: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): Call<CurrentWeatherData>

}