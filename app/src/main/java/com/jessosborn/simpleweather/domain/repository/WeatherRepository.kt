package com.jessosborn.simpleweather.domain.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jessosborn.simpleweather.R
import com.jessosborn.simpleweather.domain.Units
import com.jessosborn.simpleweather.domain.remote.OpenWeatherEndpoint
import com.jessosborn.simpleweather.domain.remote.responses.CurrentWeather
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepository constructor(
    private val context: Context,
    private val service: OpenWeatherEndpoint
) : IWeatherRepository {

    private val _currentWeatherData = MutableLiveData<CurrentWeather>()
    private val _forecastWeatherData = MutableLiveData<ForecastWeather>()
    private val _isNetworkLoading = MutableLiveData(false)
    private val _errorMessage = MutableLiveData("")

    override val currentWeather: LiveData<CurrentWeather> get() = _currentWeatherData
    override val forecastWeather: LiveData<ForecastWeather> get() = _forecastWeatherData
    override val isNetworkLoading: LiveData<Boolean> get() = _isNetworkLoading
    override val errorMessage: LiveData<String> get() = _errorMessage

    private val key = context.resources.getString(R.string.api_key)

    override fun fetchWeatherFromApi(zip: String, country: String, units: Units) {
        _isNetworkLoading.value = true
        _errorMessage.value = ""
        CoroutineScope(IO).apply {
            launch { fetchCurrentWeather(zip, country, units.name) }
            launch { fetchForecastData(zip, country, units.name) }
        }
    }

    private fun fetchForecastData(zip: String, country: String, units: String) {
        service.getForecastWeather("$zip,$country", key, units)
            .enqueue(object : Callback<ForecastWeather> {
                override fun onResponse(
                    call: Call<ForecastWeather>,
                    response: Response<ForecastWeather>,
                ) {
                    when (response.code()) {
                        200 -> _forecastWeatherData.value = response.body()
                        401 ->
                            _errorMessage.value =
                                context.getString(R.string.error_api_key)
                        404 ->
                            _errorMessage.value =
                                context.getString(R.string.error_city_not_found)
                        else -> _errorMessage.value = response.message()
                    }
                    _isNetworkLoading.value = false
                }

                override fun onFailure(call: Call<ForecastWeather>, t: Throwable) {
                    _isNetworkLoading.value = false
                    _errorMessage.value =
                        context.getString(R.string.error_weather_service_unavailable)
                }
            })
    }

    private fun fetchCurrentWeather(zip: String, country: String, units: String) {
        service.getCurrentWeather("$zip,$country", key, units)
            .enqueue(object : Callback<CurrentWeather> {
                override fun onResponse(
                    call: Call<CurrentWeather>,
                    response: Response<CurrentWeather>,
                ) {
                    when (response.code()) {
                        200 -> _currentWeatherData.value = response.body()
                        401 -> _errorMessage.value = context.getString(R.string.error_api_key)
                        404 -> _errorMessage.value =
                            context.getString(R.string.error_city_not_found)
                        else -> _errorMessage.value = response.message()
                    }
                }

                override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                    _errorMessage.value =
                        context.getString(R.string.error_weather_service_unavailable)
                }
            })
    }
}
