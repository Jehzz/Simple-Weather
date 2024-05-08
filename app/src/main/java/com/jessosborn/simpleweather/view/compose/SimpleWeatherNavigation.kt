package com.jessosborn.simpleweather.view.compose

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jessosborn.simpleweather.view.compose.screens.MainScreen
import com.jessosborn.simpleweather.view.compose.screens.SettingsScreen

@Composable
fun SimpleWeatherNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Main.route) {
        // TODO - version check, fix duplicate splash screen
        composable(route = Screen.Main.route) {
            MainScreen(
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
