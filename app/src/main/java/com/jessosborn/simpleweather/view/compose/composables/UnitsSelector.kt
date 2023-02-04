package com.jessosborn.simpleweather.view.compose.composables

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Row
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jessosborn.simpleweather.domain.Units
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme

@Composable
fun UnitsSelector(
	selectedUnits: Units,
	onClick: (Units) -> Unit,
) {
	Row {
		UnitsSelectionChip(
			unit = Units.Imperial,
			selected = selectedUnits == Units.Imperial,
			onClick = { onClick(Units.Imperial) }
		)
		UnitsSelectionChip(
			unit = Units.Metric,
			selected = selectedUnits == Units.Metric,
			onClick = { onClick(Units.Metric) }
		)
	}
}

@OptIn(ExperimentalMaterialApi::class)
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
		onClick = onClick
	) {
		Text(text = unit.name)
	}
}


@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ChipSelectorPreview() {
	SimpleWeatherTheme {
		UnitsSelector(selectedUnits = Units.Metric) {}
	}
}