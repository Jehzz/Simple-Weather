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
class WeatherViewModel @Inject constructor(
	private val weatherRepo: IWeatherRepository
) : ViewModel() {

	private val _currentWeather = MutableStateFlow<CurrentWeather?>(null)
	val currentWeather = _currentWeather.asStateFlow()

	private val _forecastWeather = MutableStateFlow<ForecastWeather?>(null)
	val forecastWeather = _forecastWeather.asStateFlow()

	private val _isNetworkLoading = MutableStateFlow(false)
	val isNetworkLoading = _isNetworkLoading.asStateFlow()

	private val _networkError = MutableSharedFlow<String>()
	val networkError = _networkError.asSharedFlow()

	fun fetchWeatherFromApi(zip: String, units: Units) {
		_isNetworkLoading.value = true
		viewModelScope.launch {
			try {
				weatherRepo.fetchCurrentData(zip, getCountryFromZip(zip), units.name)
					.onSuccess { _currentWeather.value = it }
					.onFailure { _networkError.emit(it.message.toString()) }
				weatherRepo.fetchForecastData(zip, getCountryFromZip(zip), units.name)
					.onSuccess { _forecastWeather.value = it }
					.onFailure { _networkError.emit(it.message.toString()) }
			} catch (e: Exception) {
				_networkError.emit(e.message.toString())
			}
		}
		_isNetworkLoading.value = false
	}
}
