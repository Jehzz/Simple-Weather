package com.jessosborn.simpleweather.view.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val darkColors = darkColorScheme(
	primary = Color(0xff539DE7),
	secondary = Color(0xffDF7020)
)
val lightColors = lightColorScheme(
	primary = Color(0xff1A8DFF),
	secondary = Color(0xffF57C00)
)

@Composable
fun SimpleWeatherTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	content: @Composable () -> Unit,
) {
	MaterialTheme(
		colorScheme = if (darkTheme) darkColors else lightColors,
		typography = Typography(),
		content = content
	)
}