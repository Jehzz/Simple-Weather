package com.jessosborn.simpleweather.utils

import androidx.annotation.DrawableRes
import com.jessosborn.simpleweather.R

@DrawableRes
fun getIconResource(iconCode: String): Int {
	return when (iconCode) {
		"01d" -> R.drawable.icon_01d_t
		"01n" -> R.drawable.icon_01n_t
		"02d" -> R.drawable.icon_02d_t
		"02n" -> R.drawable.icon_02n_t
		"03d" -> R.drawable.icon_03d_t
		"03n" -> R.drawable.icon_03n_t
		"04d" -> R.drawable.icon_04d_t
		"04n" -> R.drawable.icon_04n_t
		"09d" -> R.drawable.icon_09d_t
		"09n" -> R.drawable.icon_09n_t
		"10d" -> R.drawable.icon_10d_t
		"10n" -> R.drawable.icon_10n_t
		"11d" -> R.drawable.icon_11d_t
		"11n" -> R.drawable.icon_11n_t
		"13d" -> R.drawable.icon_13d_t
		"13n" -> R.drawable.icon_13n_t
		"50d" -> R.drawable.icon_50d_t
		"50n" -> R.drawable.icon_50n_t
		else -> R.drawable.icon_01d_t
	}
}