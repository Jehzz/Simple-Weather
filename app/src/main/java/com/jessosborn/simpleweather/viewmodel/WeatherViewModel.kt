package com.jessosborn.simpleweather.viewmodel

import androidx.lifecycle.ViewModel
import com.jessosborn.simpleweather.domain.repository.WeatherRepository
import com.jessosborn.simpleweather.utils.getCountryFromZip
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepo: WeatherRepository
) : ViewModel() {

    val currentWeatherDataSet by lazy { weatherRepo.currentWeather }
    val forecastWeatherDataSet by lazy { weatherRepo.forecastWeather }
    val isNetworkLoading by lazy { weatherRepo.isNetworkLoading }
    val networkError by lazy { weatherRepo.errorMessage }

    fun fetchWeatherFromApi(zip: String, units: String, key: String) {
        weatherRepo.fetchWeatherFromApi(zip, getCountryFromZip(zip), units, key)
    }
}
