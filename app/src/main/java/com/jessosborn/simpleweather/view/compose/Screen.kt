package com.jessosborn.simpleweather.view.compose

import androidx.annotation.StringRes

sealed class Screen(val route: String, @StringRes val resourceId: Int?) {
	object Main : Screen("main_screen", null)
	object Settings : Screen("settings_screen", null)

	fun withArgs(vararg args: String): String {
		return buildString {
			append(route)
			args.forEach { append("/$it") }
		}
	}
}
