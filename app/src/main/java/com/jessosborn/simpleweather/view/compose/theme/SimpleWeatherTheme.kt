package com.jessosborn.simpleweather.view.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily


@Composable
fun SimpleWeatherTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	content: @Composable () -> Unit,
) {
	MaterialTheme(
		colors = if (darkTheme) darkColors() else lightColors(),
		typography = Typography(defaultFontFamily = FontFamily.Monospace),
		content = content
	)
}

val Colors.coldColor: Color
	get() = if (isLight) Color(0xff1A8DFF) else Color(0xff539DE7)

val Colors.hotColor: Color
	get() = if (isLight) Color(0xfff57c00) else Color(0xffdf7020)