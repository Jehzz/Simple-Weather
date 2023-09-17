package com.jessosborn.simpleweather.view.compose.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColors(
	val hot: Color,
	val cold: Color
)

val LocalExtendedColors = staticCompositionLocalOf {
	ExtendedColors(
		hot = Color.Unspecified,
		cold = Color.Unspecified
	)
}

object ExtendedTheme {
	val colors: ExtendedColors
		@Composable
		get() = LocalExtendedColors.current
}