package com.jessosborn.simpleweather.view.compose.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import com.jessosborn.simpleweather.R
import com.jessosborn.simpleweather.utils.CombinedPreviews
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme

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
			.background(MaterialTheme.colorScheme.primary)
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

@CombinedPreviews
@Composable
fun SplashScreenPreview() {
	SimpleWeatherTheme {
		SplashScreen {}
	}
}
