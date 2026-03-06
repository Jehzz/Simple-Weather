package com.jessosborn.simpleweather.view.compose.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.jessosborn.simpleweather.domain.Units
import com.jessosborn.simpleweather.domain.remote.responses.CurrentWeather
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.domain.remote.responses.Main
import com.jessosborn.simpleweather.domain.remote.responses.Sys
import com.jessosborn.simpleweather.domain.remote.responses.WeatherData
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import com.jessosborn.simpleweather.domain.remote.responses.Wind
import com.jessosborn.simpleweather.utils.CombinedPreviews
import com.jessosborn.simpleweather.view.WeatherViewModel
import com.jessosborn.simpleweather.view.compose.components.CurrentWeatherInfo
import com.jessosborn.simpleweather.view.compose.components.ForecastLayout
import com.jessosborn.simpleweather.view.compose.components.ForecastPreviewParams
import com.jessosborn.simpleweather.view.compose.components.WeatherDetailDialog
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    weatherData: Map<String, WeatherViewModel.WeatherResult>,
    zipCodes: List<String>,
    preferredUnits: Units,
    isNetworkLoading: Boolean,
    networkError: String,
    refreshData: (String, Units) -> Unit,
    onSettingsClicked: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState(pageCount = { zipCodes.size })

    val pullRefreshState =
        rememberPullRefreshState(
            refreshing = isNetworkLoading,
            onRefresh = {
                if (zipCodes.isNotEmpty()) {
                    refreshData(zipCodes[pagerState.currentPage], preferredUnits)
                }
            },
        )

    var selectedWeatherSnapshot by remember { mutableStateOf<WeatherSnapshot?>(null) }

    LaunchedEffect(zipCodes) {
        zipCodes.forEach { zip ->
            if (weatherData[zip] == null) {
                refreshData(zip, preferredUnits)
            }
        }
    }

    LaunchedEffect(networkError) {
        if (networkError.isNotBlank()) {
            snackbarHostState.showSnackbar(message = networkError, duration = SnackbarDuration.Long)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = { padding ->
            Box(
                modifier = Modifier
                    .padding(bottom = padding.calculateBottomPadding())
                    .pullRefresh(pullRefreshState)
            ) {
                if (zipCodes.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No cities added yet",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onSettingsClicked) {
                            Icon(Icons.Default.Settings, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Open Settings")
                        }
                    }
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.weight(1f),
                        ) { page ->
                            val zip = zipCodes[page]
                            val data = weatherData[zip]

                            Box(
								modifier = Modifier
									.fillMaxSize()
									.verticalScroll(rememberScrollState())
							) {
                                Column {
                                    CurrentWeatherInfo(
                                        data = data?.currentWeather,
                                        preferredUnits = preferredUnits,
                                        onSettingsClicked = { onSettingsClicked() },
                                    )
                                    AnimatedVisibility(
                                        visible = data?.forecastWeather != null,
                                        enter = fadeIn(),
                                    ) {
                                        data?.forecastWeather?.let { forecast ->
                                            ForecastLayout(
                                                forecastWeather = forecast,
                                                onSnapshotSelected = { selectedWeatherSnapshot = it },
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        if (zipCodes.size > 1) {
							PageIndicator(zipCodes, pagerState)
                        }
                    }
                }

                AnimatedVisibility(
                    visible = selectedWeatherSnapshot != null,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    selectedWeatherSnapshot?.let {
                        WeatherDetailDialog(
                            weatherSnapshot = it,
                            onDismiss = { selectedWeatherSnapshot = null },
                        )
                    }
                }

                PullRefreshIndicator(
                    refreshing = isNetworkLoading,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            }
        },
    )
}

@Composable
private fun PageIndicator(
	zipCodes: List<String>,
	pagerState: PagerState
) {
	Row(
		modifier = Modifier
			.height(36.dp)
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.Center,
		verticalAlignment = Alignment.CenterVertically
	) {
		repeat(zipCodes.size) { iteration ->
			Box(
				modifier = Modifier
					.padding(4.dp)
					.clip(CircleShape)
					.background(
						color = if (pagerState.currentPage == iteration) {
							MaterialTheme.colorScheme.primary
						} else {
							MaterialTheme.colorScheme.outlineVariant
						}
					)
					.size(8.dp)
			)
		}
	}
}

@OptIn(ExperimentalMaterialApi::class)
@CombinedPreviews
@Composable
private fun Preview(
    @PreviewParameter(ForecastPreviewParams::class) forecast: ForecastWeather,
) {
    val zip = "90210"
    val currentWeather = CurrentWeather(
        name = "Hollywood",
        main = Main(
            temp = 73.38f,
            temp_min = 67.01f,
            temp_max = 76.87f,
            humidity = "78",
        ),
        sys = Sys(
            country = "US",
            sunrise = "1674998066",
            sunset = "1675036678",
        ),
        weather = listOf(
            WeatherData(
                id = 804,
                main = "Clouds",
                description = "overcast clouds",
                icon = "04d",
            ),
        ),
        wind = Wind(speed = "14.97", deg = "200"),
    )

    SimpleWeatherTheme {
        MainScreen(
            weatherData = mapOf(zip to WeatherViewModel.WeatherResult(currentWeather, forecast)),
            zipCodes = listOf(zip, "12345"),
            preferredUnits = Units.Imperial,
            isNetworkLoading = false,
            networkError = "",
            refreshData = { _, _ -> },
            onSettingsClicked = { },
        )
    }
}
