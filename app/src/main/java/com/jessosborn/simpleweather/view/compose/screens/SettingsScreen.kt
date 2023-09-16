package com.jessosborn.simpleweather.view.compose.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jessosborn.simpleweather.R
import com.jessosborn.simpleweather.domain.Theme
import com.jessosborn.simpleweather.domain.Units
import com.jessosborn.simpleweather.utils.CombinedPreviews
import com.jessosborn.simpleweather.utils.DataStoreUtil
import com.jessosborn.simpleweather.utils.isInvalidZip
import com.jessosborn.simpleweather.utils.isValidZip
import com.jessosborn.simpleweather.view.compose.composables.ThemeSelector
import com.jessosborn.simpleweather.view.compose.composables.UnitsSelector
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingsScreen(
	onSettingsEntered: () -> Unit,
) {
	val context = LocalContext.current

	val userZip by DataStoreUtil.getZip(context).collectAsState(initial = "")
	val selectedUnits by DataStoreUtil.getUnits(context).collectAsState(initial = Units.Imperial)
	val selectedTheme by DataStoreUtil.getTheme(context)
		.collectAsState(initial = Theme.FollowSystem)

	val keyboardController = LocalSoftwareKeyboardController.current
	val scope = rememberCoroutineScope()

	SettingsScreenLayout(
		userZip = userZip,
		onSettingsEntered = onSettingsEntered,
		scope = scope,
		context = context,
		keyboardController = keyboardController,
		selectedTheme = selectedTheme,
		selectedUnits = selectedUnits
	)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
private fun SettingsScreenLayout(
	userZip: String,
	onSettingsEntered: () -> Unit,
	scope: CoroutineScope,
	context: Context,
	keyboardController: SoftwareKeyboardController?,
	selectedTheme: Theme,
	selectedUnits: Units,
) {
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(stringResource(id = R.string.settings)) },
				colors = TopAppBarDefaults.smallTopAppBarColors(
					containerColor = MaterialTheme.colorScheme.primaryContainer
				)
			)
		},
		floatingActionButtonPosition = FabPosition.End,
		floatingActionButton = {
			FloatingActionButton(
				onClick = { if (userZip.isValidZip()) onSettingsEntered() }
			) {
				Icon(imageVector = Icons.Default.Save, contentDescription = "save settings")
			}
		},
		content = { padding ->
			Column(
				modifier = Modifier.padding(
					top = padding.calculateTopPadding(),
					start = 12.dp,
					end = 12.dp
				),
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.spacedBy(20.dp)
			) {
				Spacer(modifier = Modifier.height(24.dp))
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(all = 6.dp),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						modifier = Modifier
							.padding(horizontal = 12.dp),
						text = "Zip",
						style = MaterialTheme.typography.titleLarge
					)
					OutlinedTextField(
						value = userZip,
						onValueChange = { newZip ->
							scope.launch {
								DataStoreUtil.saveZip(context = context, value = newZip)
							}
						},
						modifier = Modifier.padding(horizontal = 4.dp),
						label = {},
						isError = userZip.isInvalidZip() == true,
						singleLine = true,
						keyboardActions = KeyboardActions(onDone = {
							keyboardController?.hide()
							onSettingsEntered()
						})
					)
				}
				Spacer(modifier = Modifier.height(32.dp))
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(all = 6.dp),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						modifier = Modifier
							.padding(horizontal = 12.dp),
						text = stringResource(id = R.string.theme),
						style = MaterialTheme.typography.titleLarge
					)
					ThemeSelector(
						modifier = Modifier.padding(end = 10.dp),
						selectedTheme = selectedTheme,
						onClick = { chosenTheme ->
							scope.launch {
								DataStoreUtil.saveTheme(
									context = context,
									value = chosenTheme
								)
							}
						}
					)
				}
				Spacer(modifier = Modifier.height(32.dp))
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(all = 6.dp),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						modifier = Modifier
							.padding(horizontal = 12.dp),
						text = stringResource(id = R.string.units),
						style = MaterialTheme.typography.titleLarge
					)
					UnitsSelector(
						modifier = Modifier.padding(end = 10.dp),
						selectedUnits = selectedUnits,
						onClick = { chosenUnits ->
							scope.launch {
								DataStoreUtil.saveUnits(
									context = context,
									value = chosenUnits
								)
							}
						}
					)
				}
			}
		},
	)
}

@CombinedPreviews
@Composable
fun SettingsScreenPreview() {
	SimpleWeatherTheme {
		SettingsScreen {}
	}
}
