package com.jessosborn.simpleweather.view.compose.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.jessosborn.simpleweather.domain.Units
import com.jessosborn.simpleweather.domain.remote.responses.CurrentWeather
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.domain.remote.responses.Main
import com.jessosborn.simpleweather.domain.remote.responses.Sys
import com.jessosborn.simpleweather.domain.remote.responses.WeatherData
import com.jessosborn.simpleweather.domain.remote.responses.Wind
import com.jessosborn.simpleweather.utils.DataStoreUtil
import com.jessosborn.simpleweather.view.compose.composables.CurrentWeatherInfo
import com.jessosborn.simpleweather.view.compose.composables.ForecastLayout
import com.jessosborn.simpleweather.view.compose.composables.ForecastPreviewParams
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme
import com.jessosborn.simpleweather.viewmodel.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
	onSettingsClicked: () -> Unit,
) {

	val weatherViewModel = hiltViewModel<WeatherViewModel>()

	val forecast by weatherViewModel.forecastWeatherDataSet.collectAsState(null)
	val currentWeather by weatherViewModel.currentWeatherDataSet.collectAsState(null)
	val isNetworkLoading by weatherViewModel.isNetworkLoading.collectAsState(false)
	val networkError by weatherViewModel.networkError.collectAsState("")

	val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isNetworkLoading)
	var preferredUnits by remember { mutableStateOf(Units.Imperial) }
	val scaffoldState = rememberScaffoldState()
	val context = LocalContext.current
	val scope = rememberCoroutineScope()

	// Fetch the weather, navigate to Settings if required inputs are missing
	LaunchedEffect(key1 = Unit) {
		scope.launch {
			preferredUnits = DataStoreUtil.getUnits(context)
			DataStoreUtil.getString(context = context, DataStoreUtil.USER_ZIP)?.let { zip ->
				weatherViewModel.fetchWeatherFromApi(
					zip = zip,
					units = preferredUnits
				)
			} ?: onSettingsClicked()
		}
	}
	LaunchedEffect(key1 = networkError) {
		if (networkError.isNotEmpty()) {
			scaffoldState.snackbarHostState.showSnackbar(networkError)
		}
	}
	MainScreenContent(
		currentWeather = currentWeather,
		forecast = forecast,
		preferredUnits = preferredUnits,
		swipeRefreshState = swipeRefreshState,
		scope = scope,
		onSettingsClicked = onSettingsClicked,
		refresh = weatherViewModel::fetchWeatherFromApi
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenContent(
	currentWeather: CurrentWeather?,
	forecast: ForecastWeather?,
	preferredUnits: Units,
	swipeRefreshState: SwipeRefreshState,
	scope: CoroutineScope,
	onSettingsClicked: () -> Unit,
	refresh: (String, Units) -> Unit,
) {
	val context = LocalContext.current
	Scaffold(
		topBar = {
			CurrentWeatherInfo(
				data = currentWeather,
				preferredUnits = preferredUnits,
				onSettingsClicked = { onSettingsClicked() }
			)
		},
		content = { padding ->
			SwipeRefresh(
				modifier = Modifier.padding(top = padding.calculateTopPadding()),
				state = swipeRefreshState,
				onRefresh = {
					scope.launch {
						DataStoreUtil.getString(context = context, DataStoreUtil.USER_ZIP)
							?.let { zip ->
								refresh(zip, preferredUnits)
							} ?: onSettingsClicked()
					}
				},
				content = {
					AnimatedVisibility(
						visible = forecast != null,
						enter = fadeIn()
					) {
						ForecastLayout(forecastWeather = forecast)
					}
				}
			)
		}
	)
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, device = Devices.TABLET)
@Preview(showBackground = true, device = Devices.TABLET, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun MainScreenPreview(
	@PreviewParameter(ForecastPreviewParams::class) forecast: ForecastWeather,
) {
	SimpleWeatherTheme {
		MainScreenContent(
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
			swipeRefreshState = rememberSwipeRefreshState(false),
			scope = rememberCoroutineScope(),
			onSettingsClicked = {},
			refresh = { _, _ -> },
		)
	}
}
