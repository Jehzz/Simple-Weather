package com.jessosborn.simpleweather.view.compose.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jessosborn.simpleweather.domain.Units
import com.jessosborn.simpleweather.utils.CombinedPreviews
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme

@Composable
fun UnitsSelector(
	modifier: Modifier = Modifier,
	selectedUnits: Units,
	onClick: (Units) -> Unit
) {
	Row(modifier = modifier) {
		UnitsSelectionChip(
			unit = Units.Imperial,
			selected = selectedUnits == Units.Imperial,
			onClick = { onClick(Units.Imperial) }
		)
		Spacer(modifier = Modifier.width(8.dp))
		UnitsSelectionChip(
			unit = Units.Metric,
			selected = selectedUnits == Units.Metric,
			onClick = { onClick(Units.Metric) }
		)
	}
}

@Composable
fun UnitsSelectionChip(
	unit: Units,
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
		label = { Text(text = unit.name) },
		onClick = onClick
	)
}


@CombinedPreviews
@Composable
private fun ChipSelectorPreview() {
	SimpleWeatherTheme {
		Surface {
			UnitsSelector(selectedUnits = Units.Metric) {}
		}
	}
}