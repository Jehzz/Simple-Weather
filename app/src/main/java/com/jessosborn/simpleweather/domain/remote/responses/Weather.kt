package com.jessosborn.simpleweather.domain.remote.responses

import androidx.annotation.Keep

@Keep
data class CurrentWeather(
    val name: String,
    val main: Main,
    val sys: Sys,
    val weather: List<WeatherData>,
    val wind: Wind,
)

@Keep
data class Sys(
    val country: String,
    val sunrise: String,
    val sunset: String,
)

@Keep
data class ForecastWeather(
    val list: List<WeatherSnapshot>,
)

@Keep
data class WeatherSnapshot(
    val dt: String,
    val dt_txt: String,
    val main: Main,
    val weather: List<WeatherData>,
)

@Keep
data class Main(
    val temp: Float,
    val temp_min: String,
    val temp_max: String,
    val humidity: String,
)

@Keep
data class WeatherData(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
)

@Keep
data class Wind(
    val speed: String,
    val deg: String,
)
