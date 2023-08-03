package com.jessosborn.simpleweather.utils

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
	name = "small font",
	group = "font scales",
	fontScale = 0.5f
)
@Preview(
	name = "large font",
	group = "font scales",
	fontScale = 1.5f
)
annotation class FontScalePreviews

@Preview(
	name = "day",
	group = "daynight",
	showBackground = true
)
@Preview(
	name = "night",
	group = "daynight",
	showBackground = true,
	uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class DayNightPreviews

@Preview(
	name = "small screen",
	group = "screen scales",
	showBackground = true,
	device = Devices.PIXEL
)
@Preview(
	name = "large screen",
	group = "screen scales",
	showBackground = true,
	device = Devices.TABLET
)
annotation class ScreenSizePreviews

@DayNightPreviews
@FontScalePreviews
@ScreenSizePreviews
annotation class CombinedPreviews

@Composable
fun debugPlaceholder(@DrawableRes debugPreview: Int) =
	if (LocalInspectionMode.current) painterResource(id = debugPreview) else null