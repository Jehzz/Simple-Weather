package com.jessosborn.simpleweather.view.compose.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import com.jessosborn.simpleweather.domain.remote.responses.Wind
import com.jessosborn.simpleweather.utils.CombinedPreviews
import com.jessosborn.simpleweather.utils.DataStoreUtil
import com.jessosborn.simpleweather.view.WeatherViewModel
import com.jessosborn.simpleweather.view.compose.components.CurrentWeatherInfo
import com.jessosborn.simpleweather.view.compose.components.ForecastLayout
import com.jessosborn.simpleweather.view.compose.components.ForecastPreviewParams
import com.jessosborn.simpleweather.view.compose.components.WeatherDetailDialog
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(onSettingsClicked: () -> Unit) {
	val context = LocalContext.current
	val weatherViewModel = hiltViewModel<WeatherViewModel>()

	val forecast = weatherViewModel.forecastWeather.collectAsState()
	val currentWeather = weatherViewModel.currentWeather.collectAsState()
	val isNetworkLoading = weatherViewModel.isNetworkLoading.collectAsState()

	val preferredUnits by DataStoreUtil.getUnits(context = context).collectAsState(initial = Units.Imperial)
	val userZip by DataStoreUtil.getZip(context = context).collectAsState(initial = "")

	val snackbarHostState = remember { SnackbarHostState() }

	val pullRefreshState = rememberPullRefreshState(
		refreshing = isNetworkLoading.value,
		onRefresh = { weatherViewModel.fetchWeatherFromApi(userZip, preferredUnits) }
	)

	var selectedSnapshot by remember { mutableStateOf<WeatherSnapshot?>(null) }

	// Fetch the weather, navigate to Settings if required inputs are missing
	LaunchedEffect(key1 = userZip) {
		if (userZip.isEmpty()) {
			onSettingsClicked()
		} else {
			weatherViewModel.fetchWeatherFromApi(zip = userZip, units = preferredUnits)
		}
	}
	LaunchedEffect(Unit) {
		weatherViewModel.networkError.collectLatest { errorMessage ->
			snackbarHostState.showSnackbar(message = errorMessage, duration = SnackbarDuration.Long)
		}
	}

	Scaffold(
		snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
		topBar = {
			CurrentWeatherInfo(
				data = currentWeather.value,
				preferredUnits = preferredUnits,
				onSettingsClicked = { onSettingsClicked() }
			)
		},
		content = { padding ->
			Box(
				modifier = Modifier
					.padding(padding)
					.pullRefresh(pullRefreshState)
					.verticalScroll(rememberScrollState())
			) {
				AnimatedVisibility(
					visible = selectedSnapshot != null,
					enter = fadeIn(),
					exit = fadeOut()
				) {
					selectedSnapshot?.let {
						WeatherDetailDialog(
							weatherSnapshot = it,
							onDismiss = { selectedSnapshot = null }
						)
					}
				}
				AnimatedVisibility(
					visible = forecast.value != null,
					enter = fadeIn()
				) {
					forecast.value?.let { forecast ->
						ForecastLayout(
							forecastWeather = forecast,
							onSnapshotSelected = { selectedSnapshot = it }
						)
					}
				}
				PullRefreshIndicator(
					refreshing = isNetworkLoading.value,
					state = pullRefreshState,
					modifier = Modifier.align(Alignment.TopCenter)
				)
			}
		}
	)
}

@OptIn(ExperimentalMaterialApi::class)
@CombinedPreviews
@Composable
private fun Preview(@PreviewParameter(ForecastPreviewParams::class) forecast: ForecastWeather) {
	SimpleWeatherTheme {
		val pullRefreshState = rememberPullRefreshState(refreshing = false, onRefresh = {})
		Scaffold(
			topBar = {
				CurrentWeatherInfo(
					data = CurrentWeather(
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
					preferredUnits = Units.Imperial,
					onSettingsClicked = { }
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
						ForecastLayout(
							forecastWeather = forecast,
							onSnapshotSelected = {}
						)
					}
					PullRefreshIndicator(
						refreshing = false,
						state = pullRefreshState,
						modifier = Modifier.align(Alignment.TopCenter)
					)
				}
			}
		)
	}
}