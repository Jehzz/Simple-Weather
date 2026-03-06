package com.jessosborn.simpleweather.view.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jessosborn.simpleweather.domain.Theme
import com.jessosborn.simpleweather.domain.Units
import com.jessosborn.simpleweather.utils.DataStoreUtil
import com.jessosborn.simpleweather.view.WeatherViewModel
import com.jessosborn.simpleweather.view.compose.screens.MainScreen
import com.jessosborn.simpleweather.view.compose.screens.SettingsScreen
import kotlinx.coroutines.launch

@Composable
fun SimpleWeatherNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val theme by DataStoreUtil.getTheme(context).collectAsState(initial = Theme.FollowSystem)
    val units by DataStoreUtil.getUnits(context).collectAsState(initial = Units.Imperial)
    val zipCodes by DataStoreUtil.getZips(context).collectAsState(initial = emptyList())
    val refreshTime by DataStoreUtil.getRefreshTime(context).collectAsState(initial = 2)

    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(route = Screen.Main.route) {
            val weatherViewModel = hiltViewModel<WeatherViewModel>()

            MainScreen(
                weatherData = weatherViewModel.weatherData.collectAsState().value,
                zipCodes = zipCodes,
                preferredUnits = units,
                isNetworkLoading = weatherViewModel.isNetworkLoading.collectAsState().value,
                networkError = weatherViewModel.networkError.collectAsState("").value,
                refreshData = { zip, units -> weatherViewModel.fetchWeatherForZip(zip, units) },
                onSettingsClicked = { navController.navigate(Screen.Settings.route) },
            )
        }
        composable(Screen.Settings.route) {
            val scope = rememberCoroutineScope()

            SettingsScreen(
                units = units,
                theme = theme,
                zipCodes = zipCodes,
                refreshTime = refreshTime,
                onThemeChosen = { chosenTheme -> scope.launch { DataStoreUtil.saveTheme(context, chosenTheme) } },
                onUnitsChosen = { chosenUnits -> scope.launch { DataStoreUtil.saveUnits(context, chosenUnits) } },
                onAddZip = { zip -> scope.launch { DataStoreUtil.addZip(context, zip) } },
                onRemoveZip = { zip -> scope.launch { DataStoreUtil.removeZip(context, zip) } },
                onRefreshTimeEntered = { chosenRefreshTime -> scope.launch { DataStoreUtil.saveRefreshTime(context, chosenRefreshTime) } },
                onSaveClicked = { navController.popBackStack() },
            )
        }
    }
}
