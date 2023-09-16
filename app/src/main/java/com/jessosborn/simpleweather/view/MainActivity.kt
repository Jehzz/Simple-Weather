package com.jessosborn.simpleweather.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import com.jessosborn.simpleweather.domain.Theme
import com.jessosborn.simpleweather.utils.DataStoreUtil
import com.jessosborn.simpleweather.view.compose.SimpleWeatherNavigation
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			val theme = DataStoreUtil
				.getTheme(context = applicationContext)
				.collectAsState(initial = Theme.FollowSystem)

			SimpleWeatherTheme(
				darkTheme = (theme.value == Theme.FollowSystem && isSystemInDarkTheme() || theme.value == Theme.Dark)
			) {
				SimpleWeatherNavigation()
			}
		}
	}
}
