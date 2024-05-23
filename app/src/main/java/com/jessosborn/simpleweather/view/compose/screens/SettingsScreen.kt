package com.jessosborn.simpleweather.view.compose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.jessosborn.simpleweather.R
import com.jessosborn.simpleweather.domain.Theme
import com.jessosborn.simpleweather.domain.Units
import com.jessosborn.simpleweather.utils.CombinedPreviews
import com.jessosborn.simpleweather.utils.DataStoreUtil
import com.jessosborn.simpleweather.utils.isInvalidZip
import com.jessosborn.simpleweather.utils.isValidZip
import com.jessosborn.simpleweather.view.compose.components.ThemeSelector
import com.jessosborn.simpleweather.view.compose.components.UnitsSelector
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onSettingsEntered: () -> Unit) {
	val context = LocalContext.current
	val keyboardController = LocalSoftwareKeyboardController.current

	val scope = rememberCoroutineScope()
	val textState = remember { mutableStateOf(TextFieldValue()) }

	val selectedUnits by DataStoreUtil.getUnits(context).collectAsState(initial = Units.Imperial)
	val selectedTheme by DataStoreUtil.getTheme(context).collectAsState(initial = Theme.FollowSystem)

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(stringResource(id = R.string.settings)) },
				colors = topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
			)
		},
		content = { padding ->
			Column(
				modifier = Modifier.padding(
					vertical = padding.calculateTopPadding(),
					horizontal = 12.dp,
				),
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.spacedBy(40.dp)
			) {
				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						modifier = Modifier.padding(horizontal = 12.dp),
						text = stringResource(id = R.string.theme),
						style = MaterialTheme.typography.titleLarge
					)
					ThemeSelector(
						modifier = Modifier.padding(end = 10.dp),
						selectedTheme = selectedTheme,
						onClick = { chosenTheme ->
							scope.launch {
								DataStoreUtil.saveTheme(context = context, value = chosenTheme)
							}
						}
					)
				}
				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						modifier = Modifier.padding(horizontal = 12.dp),
						text = stringResource(id = R.string.units),
						style = MaterialTheme.typography.titleLarge
					)
					UnitsSelector(
						modifier = Modifier.padding(end = 10.dp),
						selectedUnits = selectedUnits,
						onClick = { chosenUnits ->
							scope.launch {
								DataStoreUtil.saveUnits(context = context, value = chosenUnits)
							}
						}
					)
				}
				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						modifier = Modifier.padding(horizontal = 12.dp),
						text = stringResource(id = R.string.zip_code),
						style = MaterialTheme.typography.titleLarge
					)
					OutlinedTextField(
						value = textState.value,
						onValueChange = {
							textState.value = it
							if (it.text.isValidZip()) {
								scope.launch {
									DataStoreUtil.saveZip(context = context, value = it.text)
								}
							}
						},
						modifier = Modifier.padding(horizontal = 4.dp),
						label = {},
						isError = textState.value.text.isInvalidZip(),
						singleLine = true,
						keyboardActions = KeyboardActions(
							onDone = {
								keyboardController?.hide()
								onSettingsEntered()
							}
						)
					)
				}
			}
		}
	)
}

@CombinedPreviews
@Composable
private fun SettingsScreenPreview() {
	SimpleWeatherTheme {
		SettingsScreen(onSettingsEntered = {})
	}
}
