package com.jessosborn.simpleweather.viewmodel

import androidx.lifecycle.ViewModel
import com.jessosborn.simpleweather.model.repository.WeatherRepository
import com.jessosborn.simpleweather.utils.getCountryFromZip
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    private val weatherRepo: WeatherRepository
) : ViewModel() {

    val currentWeatherDataSet by lazy { weatherRepo.currentWeatherData }
    val forecastWeatherDataSet by lazy { weatherRepo.forecastWeatherData }
    val isNetworkLoading by lazy { weatherRepo.isNetworkLoading }
    val networkError by lazy { weatherRepo.errorMessage }

    fun fetchWeatherFromApi(zip: String, units: String, key: String) {
        weatherRepo.fetchWeatherFromApi(zip, getCountryFromZip(zip), units, key)
    }
}
