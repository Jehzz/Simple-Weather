package com.jessosborn.simpleweather.view.compose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jessosborn.simpleweather.R
import com.jessosborn.simpleweather.domain.Theme
import com.jessosborn.simpleweather.domain.Units
import com.jessosborn.simpleweather.utils.CombinedPreviews
import com.jessosborn.simpleweather.utils.isInvalidZip
import com.jessosborn.simpleweather.utils.isValidZip
import com.jessosborn.simpleweather.view.compose.components.ThemeSelector
import com.jessosborn.simpleweather.view.compose.components.UnitsSelector
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    theme: Theme,
    units: Units,
    zipCode: String,
    refreshTime: Int,
    onThemeChosen: (Theme) -> Unit,
    onUnitsChosen: (Units) -> Unit,
    onZipEntered: (String) -> Unit,
    onRefreshTimeEntered: (Int) -> Unit,
    onSaveClicked: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var zipCodeTextFieldValue by remember { mutableStateOf(TextFieldValue(text = zipCode)) }

    var refreshTimeTextFieldValue by remember { mutableStateOf(TextFieldValue(text = refreshTime.toString())) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.settings)) },
                colors = topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onSaveClicked() }) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
            }
        },
        content = { padding ->
            Column(
                modifier =
                    Modifier.padding(
                        vertical = padding.calculateTopPadding(),
                        horizontal = 12.dp,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(40.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        text = stringResource(id = R.string.theme),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    ThemeSelector(
                        modifier = Modifier.padding(end = 10.dp),
                        selectedTheme = theme,
                        onClick = { chosenTheme ->
                            onThemeChosen(chosenTheme)
                        },
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        text = stringResource(id = R.string.units),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    UnitsSelector(
                        modifier = Modifier.padding(end = 10.dp),
                        selectedUnits = units,
                        onClick = { chosenUnits -> onUnitsChosen(chosenUnits) },
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        text = stringResource(id = R.string.zip_code),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    OutlinedTextField(
                        value = zipCodeTextFieldValue,
                        onValueChange = {
                            zipCodeTextFieldValue = it
                            if (it.text.isValidZip()) onZipEntered(zipCodeTextFieldValue.text)
                        },
                        modifier = Modifier.padding(horizontal = 4.dp),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
                        label = {
                            if (zipCodeTextFieldValue.text.isInvalidZip()) {
                                Text(text = "Enter a valid ZipCode")
                            }
                        },
                        isError = zipCodeTextFieldValue.text.isInvalidZip(),
                        singleLine = true,
                        keyboardActions =
                            KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    onSaveClicked()
                                },
                            ),
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        text = "Auto refresh time (h)",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    OutlinedTextField(
                        value = refreshTimeTextFieldValue,
                        onValueChange = {
                            refreshTimeTextFieldValue = it
                            if (it.text.toIntOrNull() != null) onRefreshTimeEntered(refreshTimeTextFieldValue.text.toInt())
                        },
                        modifier = Modifier.padding(horizontal = 4.dp),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
                        label = {
                            if (refreshTimeTextFieldValue.text.toIntOrNull() == null) {
                                Text(text = "Enter a number")
                            }
                        },
                        isError = refreshTimeTextFieldValue.text.toIntOrNull() == null,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        keyboardActions =
                            KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    onSaveClicked()
                                },
                            ),
                    )
                }
            }
        },
    )
}

@CombinedPreviews
@Composable
private fun Preview() {
    SimpleWeatherTheme {
        SettingsScreen(
            theme = Theme.FollowSystem,
            units = Units.Metric,
            zipCode = "90210",
            refreshTime = 7,
            onThemeChosen = {},
            onUnitsChosen = {},
            onZipEntered = {},
            onRefreshTimeEntered = {},
            onSaveClicked = {},
        )
    }
}

@CombinedPreviews
@Composable
private fun ErrorPreview() {
    SimpleWeatherTheme {
        SettingsScreen(
            theme = Theme.FollowSystem,
            units = Units.Metric,
            zipCode = "9021",
            refreshTime = 7,
            onThemeChosen = {},
            onUnitsChosen = {},
            onZipEntered = {},
            onRefreshTimeEntered = {},
            onSaveClicked = {},
        )
    }
}
