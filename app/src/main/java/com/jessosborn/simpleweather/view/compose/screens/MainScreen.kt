package com.jessosborn.simpleweather.view.compose.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import com.jessosborn.simpleweather.domain.Units
import com.jessosborn.simpleweather.domain.remote.responses.CurrentWeather
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.domain.remote.responses.Main
import com.jessosborn.simpleweather.domain.remote.responses.Sys
import com.jessosborn.simpleweather.domain.remote.responses.WeatherData
import com.jessosborn.simpleweather.domain.remote.responses.Wind
import com.jessosborn.simpleweather.utils.CombinedPreviews
import com.jessosborn.simpleweather.utils.DataStoreUtil
import com.jessosborn.simpleweather.view.compose.composables.CurrentWeatherInfo
import com.jessosborn.simpleweather.view.compose.composables.ForecastLayout
import com.jessosborn.simpleweather.view.compose.composables.ForecastPreviewParams
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme
import com.jessosborn.simpleweather.viewmodel.WeatherViewModel

@Composable
fun MainScreen(onSettingsClicked: () -> Unit) {
	val weatherViewModel = hiltViewModel<WeatherViewModel>()

	val forecast = weatherViewModel.forecastWeather.collectAsState()
	val currentWeather = weatherViewModel.currentWeather.collectAsState()
	val isNetworkLoading = weatherViewModel.isNetworkLoading.collectAsState()
	val networkError = weatherViewModel.networkError.collectAsState()

	val preferredUnits by DataStoreUtil
		.getUnits(context = LocalContext.current).collectAsState(initial = Units.Imperial)
	val userZip by DataStoreUtil
		.getZip(context = LocalContext.current).collectAsState(initial = "")

	val scaffoldState = rememberScaffoldState()

	// Fetch the weather, navigate to Settings if required inputs are missing
	LaunchedEffect(key1 = userZip) {
		if (userZip.isEmpty()) {
			onSettingsClicked()
		} else {
			weatherViewModel.fetchWeatherFromApi(zip = userZip, units = preferredUnits)
		}
	}
	LaunchedEffect(key1 = networkError) {
		networkError.value?.let {
			scaffoldState.snackbarHostState.showSnackbar(it)
		}
	}
	MainScreenLayout(
		currentWeather = currentWeather.value,
		forecast = forecast.value,
		preferredUnits = preferredUnits,
		onSettingsClicked = onSettingsClicked,
		refresh = { weatherViewModel.fetchWeatherFromApi(userZip, preferredUnits) },
		isRefreshing = isNetworkLoading.value
	)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MainScreenLayout(
	currentWeather: CurrentWeather?,
	forecast: ForecastWeather?,
	preferredUnits: Units,
	isRefreshing: Boolean,
	onSettingsClicked: () -> Unit,
	refresh: () -> Unit,
) {
	val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = refresh)

	Scaffold(
		topBar = {
			CurrentWeatherInfo(
				data = currentWeather,
				preferredUnits = preferredUnits,
				onSettingsClicked = { onSettingsClicked() }
			)
		},
		content = { padding ->
			Box(
				modifier = Modifier
					.padding(padding)
					.pullRefresh(pullRefreshState)
			) {
				AnimatedVisibility(
					visible = forecast != null,
					enter = fadeIn()
				) {
					ForecastLayout(forecastWeather = forecast)
				}
				PullRefreshIndicator(
					refreshing = isRefreshing,
					state = pullRefreshState,
					modifier = Modifier.align(Alignment.TopCenter)
				)
			}
		}
	)
}

@CombinedPreviews
@Composable
private fun Preview(@PreviewParameter(ForecastPreviewParams::class) forecast: ForecastWeather) {
	SimpleWeatherTheme {
		MainScreenLayout(
			currentWeather = CurrentWeather(
				name = "Hollywood",
				main = Main(
					temp = 73.38f,
					temp_min = "67.01",
					temp_max = "76.87",
					humidity = "78"
				),
				sys = Sys(
					country = "US",
					sunrise = "1674998066",
					sunset = "1675036678"
				),
				weather = listOf(
					WeatherData(
						id = 804,
						main = "Clouds",
						description = "overcast clouds",
						icon = "04d"
					)
				),
				wind = Wind(
					speed = "14.97",
					deg = "200"
				)
			),
			forecast = forecast,
			preferredUnits = Units.Imperial,
			isRefreshing = false,
			onSettingsClicked = {},
			refresh = {},
		)
	}
}