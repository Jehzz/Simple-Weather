package com.jessosborn.simpleweather.view.compose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.jessosborn.simpleweather.utils.isValidZip
import com.jessosborn.simpleweather.view.compose.components.ThemeSelector
import com.jessosborn.simpleweather.view.compose.components.UnitsSelector
import com.jessosborn.simpleweather.view.compose.theme.SimpleWeatherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    theme: Theme,
    units: Units,
    zipCodes: List<String>,
    refreshTime: Int,
    onThemeChosen: (Theme) -> Unit,
    onUnitsChosen: (Units) -> Unit,
    onAddZip: (String) -> Unit,
    onRemoveZip: (String) -> Unit,
    onRefreshTimeEntered: (Int) -> Unit,
    onSaveClicked: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var newZipTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }
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
            LazyColumn(
                modifier =
                    Modifier
                        .padding(padding)
                        .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = stringResource(id = R.string.theme),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        ThemeSelector(
                            selectedTheme = theme,
                            onClick = { onThemeChosen(it) },
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = stringResource(id = R.string.units),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        UnitsSelector(
                            selectedUnits = units,
                            onClick = { onUnitsChosen(it) },
                        )
                    }
                }

                item {
                    Text(
                        text = "Manage Cities",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 10.dp),
                    )
                }

                items(zipCodes) { zip ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(text = zip, style = MaterialTheme.typography.bodyLarge)
                        IconButton(onClick = { onRemoveZip(zip) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove")
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        OutlinedTextField(
                            value = newZipTextFieldValue,
                            onValueChange = { newZipTextFieldValue = it },
                            modifier = Modifier.weight(1f).padding(end = 8.dp),
                            label = { Text("Add Zip Code") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                        IconButton(
                            onClick = {
                                if (newZipTextFieldValue.text.isValidZip()) {
                                    onAddZip(newZipTextFieldValue.text)
                                    newZipTextFieldValue = TextFieldValue("")
                                    keyboardController?.hide()
                                }
                            },
                            enabled = newZipTextFieldValue.text.isValidZip(),
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 80.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "Auto refresh (h)",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        OutlinedTextField(
                            value = refreshTimeTextFieldValue,
                            onValueChange = {
                                refreshTimeTextFieldValue = it
                                it.text.toIntOrNull()?.let { time -> onRefreshTimeEntered(time) }
                            },
                            modifier = Modifier.width(100.dp),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
                            isError = refreshTimeTextFieldValue.text.toIntOrNull() == null,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                    }
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
            zipCodes = listOf("90210", "10001"),
            refreshTime = 7,
            onThemeChosen = {},
            onUnitsChosen = {},
            onAddZip = {},
            onRemoveZip = {},
            onRefreshTimeEntered = {},
            onSaveClicked = {},
        )
    }
}
