package com.jessosborn.simpleweather.utils

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun debugPlaceholder(@DrawableRes debugPreview: Int) =
	if (LocalInspectionMode.current) painterResource(id = debugPreview) else null

@Preview(showBackground = true, device = Devices.PHONE)
@Preview(showBackground = true, device = Devices.PHONE, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, device = Devices.FOLDABLE)
@Preview(showBackground = true, device = Devices.FOLDABLE, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, device = Devices.TABLET)
@Preview(showBackground = true, device = Devices.TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class DevicePreviews