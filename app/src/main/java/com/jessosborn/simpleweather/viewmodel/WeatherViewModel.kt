package com.jessosborn.simpleweather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.jessosborn.simpleweather.domain.Units
import com.jessosborn.simpleweather.domain.repository.WeatherRepository
import com.jessosborn.simpleweather.utils.getCountryFromZip
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepo: WeatherRepository
) : ViewModel() {

    val currentWeatherData by lazy { weatherRepo.currentWeather.asFlow() }
    val forecastWeatherData by lazy { weatherRepo.forecastWeather.asFlow() }
    val isNetworkLoading by lazy { weatherRepo.isNetworkLoading.asFlow() }
    val networkError by lazy { weatherRepo.errorMessage.asFlow() }

    fun fetchWeatherFromApi(zip: String, units: Units) = weatherRepo.fetchWeatherFromApi(
        zip = zip,
        country = getCountryFromZip(zip),
        units = units
    )
}
