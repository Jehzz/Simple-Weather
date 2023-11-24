package com.jessosborn.simpleweather.view.compose.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun SimpleWeatherTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	content: @Composable () -> Unit
) {
	val extendedColors = ExtendedColors(
		hot = if (darkTheme) Color(0xFFB35919) else Color(0xffF57C00),
		cold = if (darkTheme) Color(0xFF3E70A1) else Color(0xff1A8DFF)
	)

	val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
	val colorScheme = when {
		dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
		dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
		darkTheme -> darkColorScheme().copy(background = Color.Black)
		else -> lightColorScheme()
	}

	CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
		MaterialTheme(
			colorScheme = colorScheme,
			typography = Typography(),
			content = content
		)
	}
}