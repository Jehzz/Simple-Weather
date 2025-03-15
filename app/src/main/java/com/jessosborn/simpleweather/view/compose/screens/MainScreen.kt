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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.jessosborn.simpleweather.domain.Units
import com.jessosborn.simpleweather.domain.remote.responses.CurrentWeather
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.domain.remote.responses.Main
import com.jessosborn.simpleweather.domain.remote.responses.Sys
import com.jessosborn.simpleweather.domain.remote.responses.WeatherData
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import com.jessosborn.simpleweather.domain.remote.responses.Wind
import com.jessosborn.simpleweather.utils.CombinedPreviews
import com.jessosborn.simpleweather.view.compose.components.CurrentWeatherInfo
import com.jessosborn.simpleweather.view.compose.components.ForecastLayout
import com.jessosborn.simpleweather.view.compose.components.ForecastPreviewParams
import com.jessosborn.simpleweather.view.compose.components.WeatherDetailDialog
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
	currentWeather: CurrentWeather? = null,
	forecastWeather: ForecastWeather? = null,
	userZip: String,
	preferredUnits: Units,
	isNetworkLoading: Boolean,
	networkError: String,
	refreshData: (String, Units) -> Unit,
	onSettingsClicked: () -> Unit
) {

	val snackbarHostState = remember { SnackbarHostState() }

	val pullRefreshState = rememberPullRefreshState(
		refreshing = isNetworkLoading,
		onRefresh = { refreshData(userZip, preferredUnits) }
	)

	var selectedWeatherSnapshot by remember { mutableStateOf<WeatherSnapshot?>(null) }

	LaunchedEffect(key1 = userZip) {
		if (userZip.isNotBlank()) {
			refreshData(userZip, preferredUnits)
		}
	}
	LaunchedEffect(networkError) {
		if (networkError.isNotBlank()) {
			snackbarHostState.showSnackbar(message = networkError, duration = SnackbarDuration.Long)
		}
	}

	Scaffold(
		snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
					.verticalScroll(rememberScrollState())
			) {
				AnimatedVisibility(
					visible = selectedWeatherSnapshot != null,
					enter = fadeIn(),
					exit = fadeOut()
				) {
					selectedWeatherSnapshot?.let {
						WeatherDetailDialog(
							weatherSnapshot = it,
							onDismiss = { selectedWeatherSnapshot = null }
						)
					}
				}
				AnimatedVisibility(
					visible = forecastWeather != null,
					enter = fadeIn()
				) {
					forecastWeather?.let { forecast ->
						ForecastLayout(
							forecastWeather = forecast,
							onSnapshotSelected = { selectedWeatherSnapshot = it }
						)
					}
				}
				PullRefreshIndicator(
					refreshing = isNetworkLoading,
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
		MainScreen(
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
				wind = Wind(speed = "14.97", deg = "200")
			),
			forecastWeather = forecast,
			userZip = "90210",
			preferredUnits = Units.Imperial,
			isNetworkLoading = false,
			networkError = "",
			refreshData = { _, _ -> },
			onSettingsClicked = { }
		)
	}
}