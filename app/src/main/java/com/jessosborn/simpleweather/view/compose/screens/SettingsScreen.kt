package com.jessosborn.simpleweather.view.compose.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jessosborn.simpleweather.R
import com.jessosborn.simpleweather.domain.Units
import com.jessosborn.simpleweather.utils.DataStoreUtil
import com.jessosborn.simpleweather.utils.DataStoreUtil.USER_ZIP
import com.jessosborn.simpleweather.utils.isInvalidZip
import com.jessosborn.simpleweather.utils.isValidZip
import com.jessosborn.simpleweather.view.compose.composables.UnitsSelector
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme
import com.jessosborn.simpleweather.view.compose.theme.coldColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingsScreen(
	onSettingsEntered: () -> Unit,
) {

	var zip by rememberSaveable { mutableStateOf("") }
	var selectedUnits by rememberSaveable { mutableStateOf(Units.Imperial) }

	val context = LocalContext.current
	val keyboardController = LocalSoftwareKeyboardController.current
	val scope = rememberCoroutineScope()

	LaunchedEffect(key1 = Unit) {
		zip = DataStoreUtil.getString(context, USER_ZIP).orEmpty()
		selectedUnits = DataStoreUtil.getUnits(context)
	}

	Scaffold(
		scaffoldState = rememberScaffoldState(),
		topBar = {
			TopAppBar(
				title = { Text(stringResource(id = R.string.settings)) },
				backgroundColor = MaterialTheme.colors.coldColor
			)
		},
		floatingActionButtonPosition = FabPosition.End,
		floatingActionButton = {
			FloatingActionButton(onClick = {
				if (zip.isValidZip()) {
					onSettingsEntered()
				}
			}) {
				Icon(imageVector = Icons.Default.Save, contentDescription = "save settings")
			}
		},
		content = { padding ->
			Column(
				modifier = Modifier
					.fillMaxSize(),
				horizontalAlignment = Alignment.CenterHorizontally
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
						text = stringResource(id = R.string.units),
						style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
					)
					UnitsSelector(
						selectedUnits = selectedUnits,
						onClick = { chosenUnits ->
							selectedUnits = chosenUnits
							scope.launch {
								DataStoreUtil.saveUnits(
									context = context,
									value = selectedUnits
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
						text = "Zip",
						style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
					)
					TextField(
						value = zip,
						onValueChange = {
							zip = it
							if (zip.isValidZip()) {
								scope.launch {
									DataStoreUtil.saveString(
										context = context,
										key = USER_ZIP,
										value = zip
									)
								}
							}
						},
						modifier = Modifier.padding(horizontal = 4.dp),
						label = { Text(text = stringResource(id = R.string.enter_zip_code)) },
						isError = zip.isInvalidZip(),
						singleLine = true,
						keyboardActions = KeyboardActions(onDone = {
							keyboardController?.hide()
							onSettingsEntered()
						})
					)
				}
			}
		},
	)
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingsScreenPreview() {
	SimpleWeatherTheme {
		SettingsScreen {}
	}
}
