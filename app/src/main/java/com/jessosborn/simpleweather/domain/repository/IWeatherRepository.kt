package com.jessosborn.simpleweather.domain.repository

import androidx.lifecycle.LiveData
import com.jessosborn.simpleweather.domain.remote.responses.CurrentWeather
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather

interface IWeatherRepository {

    val currentWeather: LiveData<CurrentWeather>
    val forecastWeather: LiveData<ForecastWeather>
    val isNetworkLoading: LiveData<Boolean>
    val errorMessage: LiveData<String>

    fun fetchWeatherFromApi(zip: String, country: String, units: String, key: String)
}
