package com.jessosborn.simpleweather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jessosborn.simpleweather.domain.Units
import com.jessosborn.simpleweather.domain.remote.responses.CurrentWeather
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.domain.repository.WeatherRepository
import com.jessosborn.simpleweather.utils.getCountryFromZip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepo: WeatherRepository
) : ViewModel() {

    var currentWeather: MutableLiveData<CurrentWeather> = MutableLiveData(null)
    var forecastWeather: MutableLiveData<ForecastWeather> = MutableLiveData(null)
    var isNetworkLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var networkError: MutableLiveData<String> = MutableLiveData(null)

    fun fetchWeatherFromApi(zip: String, units: Units) {
        isNetworkLoading.postValue(true)
        viewModelScope.launch {
            weatherRepo.fetchCurrentData(zip, getCountryFromZip(zip), units.name)
                .onSuccess { currentWeather.postValue(it) }
                .onFailure { networkError.postValue("Error") }
            weatherRepo.fetchForecastData(zip, getCountryFromZip(zip), units.name)
                .onSuccess { forecastWeather.postValue(it) }
                .onFailure { networkError.postValue("Error") }
        }
        isNetworkLoading.postValue(false)
    }
}
