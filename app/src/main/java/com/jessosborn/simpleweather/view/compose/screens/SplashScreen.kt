package com.jessosborn.simpleweather.view.compose.screens

import android.content.res.Configuration
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.jessosborn.simpleweather.R
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme
import com.jessosborn.simpleweather.view.compose.theme.coldColor

@Composable
fun SplashScreen(
	onLoadComplete: () -> Unit,
) {
	val scale = remember { Animatable(1f) }
	LaunchedEffect(key1 = Unit) {
		scale.animateTo(
			targetValue = 2f,
			animationSpec = tween(
				durationMillis = 400,
				easing = { OvershootInterpolator(3f).getInterpolation(it) }
			)
		)
		onLoadComplete()

	}
	Box(
		modifier = Modifier
			.background(MaterialTheme.colors.coldColor)
			.fillMaxSize(1f),
		contentAlignment = Alignment.Center,
	) {
		Image(
			painter = painterResource(id = R.mipmap.ic_launcher_foreground),
			contentDescription = null,
			modifier = Modifier.scale(scale.value)
		)
	}
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SplashScreenPreview() {
	SimpleWeatherTheme {
		SplashScreen(onLoadComplete = { })
	}
}
