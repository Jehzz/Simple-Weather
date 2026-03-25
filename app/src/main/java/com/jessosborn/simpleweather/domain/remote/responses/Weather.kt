package com.jessosborn.simpleweather.domain.remote.responses

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

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
@Entity(tableName = "weather_snapshot")
data class WeatherSnapshot(
    // Add columns for zip and units to identify the forecast this snapshot belongs to.
    val zip: String = "",
    val units: String = "",
    @PrimaryKey
    val dt: String,
    val dt_txt: String, // Time of data forecasted, ISO, UTC
    val main: Main,
    val weather: List<WeatherData>,
    val pop: Float,
    val rain: Rain? = null,
    val snow: Snow? = null, // Added optional snow field
    var createdAt: Long = System.currentTimeMillis(),
)

@Keep
data class Main(
    val temp: Float,
    val temp_min: Float,
    val temp_max: Float,
    val humidity: String,
)

class MainTypeConverter {
    @TypeConverter
    fun fromMain(main: Main): String {
        return "${main.temp},${main.temp_min},${main.temp_max},${main.humidity}"
    }

    @TypeConverter
    fun toMain(main: String): Main {
        val parts = main.split(",")
        return Main(parts[0].toFloat(), parts[1].toFloat(), parts[2].toFloat(), parts[3])
    }
}

@Keep
@Entity(tableName = "weather_data")
data class WeatherData(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
)

class WeatherTypeConverter {
    @TypeConverter
    fun fromWeatherData(weatherData: List<WeatherData>): String {
        return weatherData.joinToString(";") { "${it.id},${it.main},${it.description},${it.icon}" }
    }

    @TypeConverter
    fun toWeatherData(weatherData: String): List<WeatherData>? {
        if (weatherData.isEmpty()) {
            return emptyList()
        }
        return weatherData.split(";").map {
            if (it.isEmpty()) {
                return@map WeatherData(id = 0, main = "", description = "", icon = "")
            }
            val parts = it.split(",")
            if (parts.size < 4) return null
            WeatherData(id = parts[0].toInt(), main = parts[1], description = parts[2], icon = parts[3])
        }
    }
}

@Keep
data class Wind(
    val speed: String,
    val deg: String,
)

@Keep
data class Rain(
    val `3h`: Float,
)

class RainTypeConverter {
    @TypeConverter
    fun fromRain(rain: Rain?): String? {
        return rain?.`3h`?.toString()
    }

    @TypeConverter
    fun toRain(rain: String?): Rain? {
        return rain?.toFloatOrNull()?.let { Rain(it) }
    }
}

@Keep
data class Snow(
    val `3h`: Float,
)

class SnowTypeConverter {
    @TypeConverter
    fun fromSnow(snow: Snow?): String? {
        return snow?.`3h`?.toString()
    }

    @TypeConverter
    fun toSnow(snow: String?): Snow? {
        return snow?.toFloatOrNull()?.let { Snow(it) }
    }
}
