package com.jessosborn.simpleweather.view.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jessosborn.simpleweather.domain.Theme
import com.jessosborn.simpleweather.utils.DayNightPreviews
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme

@Composable
fun ThemeSelector(
	modifier: Modifier = Modifier,
	selectedTheme: Theme,
	onClick: (Theme) -> Unit
) {
	Column(
		modifier = modifier,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		ThemeSelectionChip(
			theme = Theme.FollowSystem,
			selected = selectedTheme == Theme.FollowSystem,
			onClick = { onClick(Theme.FollowSystem) }
		)
		Row {
			ThemeSelectionChip(
				theme = Theme.Dark,
				selected = selectedTheme == Theme.Dark,
				onClick = { onClick(Theme.Dark) }
			)
			Spacer(modifier = Modifier.width(8.dp))
			ThemeSelectionChip(
				theme = Theme.Light,
				selected = selectedTheme == Theme.Light,
				onClick = { onClick(Theme.Light) }
			)
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSelectionChip(
	theme: Theme,
	selected: Boolean,
	onClick: () -> Unit,
) {
	FilterChip(
		selected = selected,
		leadingIcon = {
			if (selected) Icon(
				imageVector = Icons.Filled.Check,
				contentDescription = null
			)
		},
		label = { Text(text = theme.name) },
		onClick = onClick
	)
}


@DayNightPreviews
@Composable
private fun Preview() {
	SimpleWeatherTheme {
		ThemeSelector(selectedTheme = Theme.FollowSystem) {}
	}
}