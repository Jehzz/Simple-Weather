package com.jessosborn.simpleweather.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jessosborn.simpleweather.domain.Units
import com.jessosborn.simpleweather.domain.remote.responses.CurrentWeather
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.domain.repository.IWeatherRepository
import com.jessosborn.simpleweather.utils.getCountryFromZip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel
    @Inject
    constructor(
        private val weatherRepo: IWeatherRepository,
    ) : ViewModel() {
        private val _weatherData = MutableStateFlow<Map<String, WeatherResult>>(emptyMap())
        val weatherData = _weatherData.asStateFlow()

        private val _isNetworkLoading = MutableStateFlow(false)
        val isNetworkLoading = _isNetworkLoading.asStateFlow()

        private val _networkError = MutableSharedFlow<String>()
        val networkError = _networkError.asSharedFlow()

        fun fetchWeatherForZip(
            zip: String,
            units: Units,
        ) {
            viewModelScope.launch {
                _isNetworkLoading.value = true
                try {
                    val currentResult = weatherRepo.fetchCurrentData(zip, getCountryFromZip(zip), units.name)
                    val forecastResult = weatherRepo.fetchForecastData(zip, getCountryFromZip(zip), units.name)

                    val current = currentResult.getOrNull()
                    val forecast = forecastResult.getOrNull()

                    if (current != null && forecast != null) {
                        _weatherData.value = _weatherData.value + (zip to WeatherResult(current, forecast))
                    }

                    currentResult.onFailure { _networkError.emit(it.message.toString()) }
                    forecastResult.onFailure { _networkError.emit(it.message.toString()) }
                } catch (e: Exception) {
                    _networkError.emit(e.message.toString())
                } finally {
                    _isNetworkLoading.value = false
                }
            }
        }

        data class WeatherResult(
            val currentWeather: CurrentWeather,
            val forecastWeather: ForecastWeather,
        )
    }
