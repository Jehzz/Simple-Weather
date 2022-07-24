package com.jessosborn.simpleweather.model.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jessosborn.simpleweather.R
import com.jessosborn.simpleweather.model.data.CurrentWeather
import com.jessosborn.simpleweather.model.data.ForecastWeather
import com.jessosborn.simpleweather.model.network.Network
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val network: Network
) {

    private val _currentWeatherData = MutableLiveData<CurrentWeather>()
    private val _forecastWeatherData = MutableLiveData<ForecastWeather>()
    private val _isNetworkLoading = MutableLiveData<Boolean>()
    private val _errorMessage = MutableLiveData<String>()

    val currentWeather: LiveData<CurrentWeather> get() = _currentWeatherData
    val forecastWeather: LiveData<ForecastWeather> get() = _forecastWeatherData
    val isNetworkLoading: LiveData<Boolean> get() = _isNetworkLoading
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchWeatherFromApi(zip: String, country: String, units: String, key: String) {
        _isNetworkLoading.value = true
        CoroutineScope(IO).launch {
            withContext(this.coroutineContext) { fetchCurrentWeather(zip, country, key, units) }
            withContext(this.coroutineContext) { fetchForecastData(zip, country, key, units) }
        }
    }

    private fun fetchForecastData(zip: String, country: String, key: String, units: String,) {
        network.initRetrofit().getForecastWeather("$zip,$country", key, units)
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

    private fun fetchCurrentWeather(zip: String, country: String, key: String, units: String,) {
        network.initRetrofit().getCurrentWeather("$zip,$country", key, units)
            .enqueue(object : Callback<CurrentWeather> {
                override fun onResponse(
                    call: Call<CurrentWeather>,
                    response: Response<CurrentWeather>,
                ) {
                    when (response.code()) {
                        200 -> _currentWeatherData.value = response.body()
                        401 -> _errorMessage.value = context.getString(R.string.error_api_key)
                        404 -> _errorMessage.value = context.getString(R.string.error_city_not_found)
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
