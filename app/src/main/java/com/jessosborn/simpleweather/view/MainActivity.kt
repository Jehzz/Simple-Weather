package com.jessosborn.simpleweather.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.jessosborn.simpleweather.view.compose.SimpleWeatherNavigation
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			SimpleWeatherTheme {
				SimpleWeatherNavigation()
			}
		}
	}
}
