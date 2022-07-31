package com.jessosborn.simpleweather.domain.remote.responses

data class CurrentWeather(
    val name: String,
    val main: Main,
    val weather: List<WeatherData>,
    val wind: Wind,
)

data class ForecastWeather(
    val list: List<WeatherList>,
)

data class WeatherList(
    val dt: String,
    val dt_txt: String,
    val main: Main,
    val weather: List<WeatherData>,
)

data class Main(
    val temp: Float,
    val temp_min: String,
    val temp_max: String,
    val humidity: String,
)

data class WeatherData(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
)

data class Wind(
    val speed: String,
    val deg: String,
)
