package com.jessosborn.simpleweather.view.compose

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jessosborn.simpleweather.view.compose.screens.MainScreen
import com.jessosborn.simpleweather.view.compose.screens.SettingsScreen
import com.jessosborn.simpleweather.view.compose.screens.SplashScreen
import com.jessosborn.simpleweather.viewmodel.WeatherViewModel

@Composable
fun SimpleWeatherNavigation() {
    val viewModel = viewModel<WeatherViewModel>()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        // TODO - version check, fix duplicate splash screen
        composable(route = Screen.Splash.route) {
            SplashScreen(
                onLoadComplete = {
                    navController.popBackStack()
                    navController.navigate(Screen.Main.route)
                }
            )
        }
        composable(route = Screen.Main.route) {
            MainScreen(
                weatherViewModel = viewModel,
                onSettingsClicked = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onSettingsEntered = { navController.popBackStack() }
            )
        }
    }
}
